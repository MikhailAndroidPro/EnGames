package com.example.engames.presentation.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.example.domain.models.UserProfile
import com.example.domain.models.enums.Gender
import com.example.engames.R
import com.example.engames.databinding.FragmentProfileBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.ProfileViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
), TextWatcher {
    private var isPasswordHidden = true
    private val imageChanged = false
    override val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUserProfile()
        setUpView()
    }

    fun setUpView() {
        with(binding) {
            Glide.with(userImg)
                .load("")
                .placeholder(resources.getDrawable(R.drawable.user_image_placeholder))
                .into(userImg)
            userNameEditText.addTextChangedListener(this@ProfileFragment)
            emailEditText.addTextChangedListener(this@ProfileFragment)
            newPasswordEditText.addTextChangedListener(this@ProfileFragment)
        }
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.user.observe(viewLifecycleOwner) { user ->
            user.let {
                binding.userNameEditText.setText(it.username)
                binding.emailEditText.setText(it.email)
                binding.newPasswordEditText.setText("")
                binding.genderBtn.text = it.gender.name
                binding.genderBtn.setBackgroundDrawable(
                    if (binding.genderBtn.text.toString() == Gender.Male.name) resources.getDrawable(
                        R.drawable.male_background
                    )
                    else resources.getDrawable(R.drawable.female_background)
                )
            }
        }
    }

    private fun checkUserChanges() {
        val currentUsername = binding.userNameEditText.text.toString()
        val currentEmail = binding.emailEditText.text.toString()
        val currentGender = binding.genderBtn.text.toString()
        val newPassword = binding.newPasswordEditText.text.toString()

        val initial = viewModel.user.value ?: return

        val hasChanged =
            currentUsername != initial.username ||
                    (currentEmail != initial.email) ||
                    currentGender != initial.gender.name || imageChanged || newPassword.length >= 6
        val canSave = hasChanged && Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.text).matches()

        binding.saveChangesButton.isEnabled = canSave
        binding.saveChangesButton.alpha = if (canSave) 1.0f else 0.7f
    }

    override fun applyClick() {
        super.applyClick()
        with(binding) {
            genderBtn.setOnClickListener {
                if (genderBtn.text.toString() == Gender.Male.name) {
                    genderBtn.text = Gender.Female.name
                    genderBtn.setBackgroundDrawable(resources.getDrawable(R.drawable.female_background))
                } else {
                    genderBtn.text = Gender.Male.name
                    genderBtn.setBackgroundDrawable(resources.getDrawable(R.drawable.male_background))
                }
                checkUserChanges()
            }
            eyeBtn.setOnClickListener {
                isPasswordHidden = !isPasswordHidden
                eyeBtn.setImageResource(
                    if (isPasswordHidden) R.drawable.eye_close else R.drawable.eye_open
                )
                newPasswordEditText.transformationMethod =
                    if (isPasswordHidden) PasswordTransformationMethod.getInstance() else null
                newPasswordEditText.setSelection(newPasswordEditText.text.length)
            }
            saveChangesButton.setOnClickListener {

            }
            userImg.setOnClickListener {

            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        checkUserChanges()
    }
}