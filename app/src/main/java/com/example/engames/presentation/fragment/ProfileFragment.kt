package com.example.engames.presentation.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.domain.models.UserProfile
import com.example.domain.models.enums.Gender
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentProfileBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/** Manages user profile display and editing. */
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate),
    TextWatcher {

    override val viewModel: ProfileViewModel by viewModels()
    private var isPasswordHidden = true
    private var imageChanged = false
    private lateinit var currentUserProfile: UserProfile
    private lateinit var updatedUser: UserProfile

    /** Launches camera permission request. */
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pickFromCamera.launch(null)
        }

    private val pickFromGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        /** Handles image selection from gallery. */
        uri?.let {
            viewModel.selectedImageUri = it
            viewModel.selectedBitmap = null
            updateImage(it.toString())
        }
    }

    private val pickFromCamera = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            /** Handles image capture from camera. */
            bitmap?.let {
                viewModel.selectedBitmap = it
                viewModel.selectedImageUri = null
                Glide.with(binding.userImg).load(it).into(binding.userImg)
                imageChanged = true
                checkUserChanges()
            }
        }

    /** Initializes view and fetches user profile. */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserProfile()
        binding.userNameEditText.addTextChangedListener(this@ProfileFragment)
        binding.emailEditText.addTextChangedListener(this@ProfileFragment)
        binding.newPasswordEditText.addTextChangedListener(this@ProfileFragment)
    }

    /** Sets up initial view with user data. */
    private fun setupView() = with(binding) {
        Glide.with(userImg)
            .load(currentUserProfile.image)
            .error(R.drawable.user_image_placeholder)
            .into(userImg)

        userNameEditText.setText(currentUserProfile.username)
        emailEditText.setText(currentUserProfile.email)
        if (currentUserProfile.gender == 2) toggleGender()
    }

    /** Observes LiveData for user profile, state, and photo updates. */
    override fun setObservers() {
        super.setObservers()
        viewModel.user.observe(viewLifecycleOwner) { task ->
            when (task) {
                is ResponseState.Success -> {
                    currentUserProfile = task.data
                    setupView()
                }

                is ResponseState.Error -> {
                    showToast(task.message)
                }
            }
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    currentUserProfile = updatedUser
                    binding.newPasswordEditText.text.clear()
                    checkUserChanges()
                }

                is ResponseState.Error -> {
                    showToast(it.message)
                }
            }
        }
        viewModel.photo.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    imageChanged = false
                    showToast(resources.getString(R.string.image_loaded))
                    checkUserChanges()
                }

                is ResponseState.Error -> {
                    showToast(it.message)
                }
            }
        }
    }

    /** Checks for changes in user profile and updates save button state. */
    private fun checkUserChanges() {
        val initial = currentUserProfile
        val currentUsername = binding.userNameEditText.text.toString()
        val currentEmail = binding.emailEditText.text.toString()
        val currentGender = binding.genderBtn.text.toString()
        val newPassword = binding.newPasswordEditText.text.toString()

        val hasChanged = currentUsername != initial.username ||
                currentEmail != initial.email ||
                currentGender != Gender.entries.firstOrNull { it.value == initial.gender }?.name ||
                imageChanged ||
                newPassword.length >= 6

        val canSave = hasChanged && Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()
        binding.saveChangesButton.apply {
            isEnabled = canSave
            alpha = if (canSave) 1.0f else 0.7f
        }
    }

    /** Toggles user gender and updates UI. */
    private fun toggleGender() = with(binding.genderBtn) {
        val newGender = if (text == Gender.Male.name) Gender.Female else Gender.Male
        text = newGender.name
        setBackgroundResource(
            if (newGender == Gender.Male) R.drawable.male_background else R.drawable.female_background
        )
        checkUserChanges()
    }

    /** Toggles password visibility in the input field. */
    private fun togglePasswordVisibility() = with(binding) {
        isPasswordHidden = !isPasswordHidden
        eyeBtn.setImageResource(if (isPasswordHidden) R.drawable.eye_close else R.drawable.eye_open)
        newPasswordEditText.transformationMethod =
            if (isPasswordHidden) PasswordTransformationMethod.getInstance() else null
        newPasswordEditText.setSelection(newPasswordEditText.text.length)
    }

    /** Shows a dialog to choose image source (camera or gallery). */
    private fun imagePicker() {
        val items = arrayOf(getString(R.string.Camera), getString(R.string.gallery))
        MaterialAlertDialogBuilder(context())
            .setTitle(getString(R.string.pick_image_resource))
            .setItems(items) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndLaunch()
                    1 -> pickFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
            .show()
    }

    /** Updates the displayed user image. */
    private fun updateImage(uri: String) {
        Glide.with(binding.userImg).load(uri)
            .placeholder(R.drawable.user_image_placeholder)
            .into(binding.userImg)
        imageChanged = true
        checkUserChanges()
    }

    /** Checks for camera permission and launches camera if granted. */
    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> pickFromCamera.launch(null)

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showPermissionRationaleDialog()

            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    /** Shows a dialog explaining why camera permission is needed. */
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.camera_permission))
            .setMessage(getString(R.string.access_camera))
            .setPositiveButton(getString(R.string.allow)) { _, _ ->
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    /** Sets up click listeners for UI elements. */
    override fun applyClick() = with(binding) {
        super.applyClick()
        genderBtn.setOnClickListener {
            toggleGender()
            checkUserChanges()
        }

        eyeBtn.setOnClickListener {
            togglePasswordVisibility()
        }

        saveChangesButton.setOnClickListener {
            updatedUser = UserProfile(
                binding.userNameEditText.text.toString(),
                binding.emailEditText.text.toString(),
                if (currentUserProfile.oldHash == binding.newPasswordEditText.text.toString()
                        .hashCode()
                ) "" else binding.newPasswordEditText.text.toString(),
                Gender.entries.first { it.name == binding.genderBtn.text }.value,
                currentUserProfile.image
            )
            viewModel.saveUser(context(), updatedUser, imageChanged)
        }

        userImg.setOnClickListener { imagePicker() }
    }

    /** Called after text in editable fields changes. */
    override fun afterTextChanged(s: Editable?) = checkUserChanges()
    /** Called before text in editable fields changes. */
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
