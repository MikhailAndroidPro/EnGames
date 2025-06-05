package com.example.engames.presentation.fragment

import android.Manifest
import android.content.pm.PackageManager
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
import com.bumptech.glide.Glide
import com.example.domain.models.enums.Gender
import com.example.engames.R
import com.example.engames.databinding.FragmentProfileBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.ProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate), TextWatcher {

    override val viewModel: ProfileViewModel by viewModels()
    private var isPasswordHidden = true
    private var imageChanged = false

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) pickFromCamera.launch(null)
        }

    private val pickFromGallery =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                updateImage(it.toString())
                Log.d("PhotoPicker", "Selected URI: $uri")
            } ?: Log.d("PhotoPicker", "No media selected")
        }

    private val pickFromCamera =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                Glide.with(binding.userImg).load(bitmap).into(binding.userImg)
                imageChanged = true
                checkUserChanges()
            } ?: Log.d("PhotoPicker", "No media selected")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserProfile()
        setupView()
    }

    private fun setupView() = with(binding) {
        Glide.with(userImg)
            .load(R.drawable.user_image_placeholder)
            .into(userImg)

        userNameEditText.addTextChangedListener(this@ProfileFragment)
        emailEditText.addTextChangedListener(this@ProfileFragment)
        newPasswordEditText.addTextChangedListener(this@ProfileFragment)

        genderBtn.setOnClickListener {
            toggleGender()
            checkUserChanges()
        }

        eyeBtn.setOnClickListener {
            togglePasswordVisibility()
        }

        saveChangesButton.setOnClickListener {
            //
        }

        userImg.setOnClickListener { imagePicker() }
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.apply {
                userNameEditText.setText(user.username)
                emailEditText.setText(user.email)
                newPasswordEditText.setText("")
                genderBtn.text = user.gender.name
                genderBtn.setBackgroundResource(
                    if (user.gender == Gender.Male) R.drawable.male_background
                    else R.drawable.female_background
                )
            }
        }
    }

    private fun checkUserChanges() {
        val initial = viewModel.user.value ?: return
        val currentUsername = binding.userNameEditText.text.toString()
        val currentEmail = binding.emailEditText.text.toString()
        val currentGender = binding.genderBtn.text.toString()
        val newPassword = binding.newPasswordEditText.text.toString()

        val hasChanged = currentUsername != initial.username ||
                currentEmail != initial.email ||
                currentGender != initial.gender.name ||
                imageChanged ||
                newPassword.length >= 6

        val canSave = hasChanged && Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()
        binding.saveChangesButton.apply {
            isEnabled = canSave
            alpha = if (canSave) 1.0f else 0.7f
        }
    }

    private fun toggleGender() = with(binding.genderBtn) {
        val newGender = if (text == Gender.Male.name) Gender.Female else Gender.Male
        text = newGender.name
        setBackgroundResource(
            if (newGender == Gender.Male) R.drawable.male_background else R.drawable.female_background
        )
    }

    private fun togglePasswordVisibility() = with(binding) {
        isPasswordHidden = !isPasswordHidden
        eyeBtn.setImageResource(if (isPasswordHidden) R.drawable.eye_close else R.drawable.eye_open)
        newPasswordEditText.transformationMethod =
            if (isPasswordHidden) PasswordTransformationMethod.getInstance() else null
        newPasswordEditText.setSelection(newPasswordEditText.text.length)
    }

    private fun imagePicker() {
        val items = arrayOf(getString(R.string.Camera), getString(R.string.gallery))
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.pick_image_resource))
            .setItems(items) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndLaunch()
                    1 -> pickFromGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
            .show()
    }

    private fun updateImage(uri: String) {
        Glide.with(binding.userImg).load(uri)
            .placeholder(R.drawable.user_image_placeholder)
            .into(binding.userImg)
        imageChanged = true
        checkUserChanges()
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> pickFromCamera.launch(null)

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showPermissionRationaleDialog()

            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

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

    override fun afterTextChanged(s: Editable?) = checkUserChanges()
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
