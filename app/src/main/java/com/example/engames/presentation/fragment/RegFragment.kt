package com.example.engames.presentation.fragment

import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.databinding.FragmentRegBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.RegViewModel
import kotlinx.coroutines.launch


class RegFragment : BaseFragment<FragmentRegBinding>(
    FragmentRegBinding::inflate
) {
    private var isPasswordHidden = true
    private var isRepeatPasswordHidden = true
    override val viewModel: RegViewModel by viewModels()

    override fun applyClick() {
        super.applyClick()
        with(binding) {
            signInText.setOnClickListener {
                findNavController().popBackStack()
            }
            signUpButton.setOnClickListener {
                registerUser()
            }
            eyeBtnPassword.setOnClickListener {
                isPasswordHidden = !isPasswordHidden
                eyeBtnPassword.setImageResource(
                    if (isPasswordHidden) R.drawable.eye_close else R.drawable.eye_open
                )
                passwordEditText.transformationMethod =
                    if (isPasswordHidden) PasswordTransformationMethod.getInstance() else null
                passwordEditText.setSelection(passwordEditText.text.length)
            }
            eyeBtnRepeatPassword.setOnClickListener {
                isRepeatPasswordHidden = !isRepeatPasswordHidden
                eyeBtnRepeatPassword.setImageResource(
                    if (isRepeatPasswordHidden) R.drawable.eye_close else R.drawable.eye_open
                )
                repeatPasswordEditText.transformationMethod =
                    if (isRepeatPasswordHidden) PasswordTransformationMethod.getInstance() else null
                repeatPasswordEditText.setSelection(repeatPasswordEditText.text.length)
            }
        }
    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val repeatPassword = binding.repeatPasswordEditText.text.toString()
        val username = binding.userNameEditText.text.toString()
        val isChecked = binding.checkboxAgreeTermsConditions.isChecked

        fun checkData(): Boolean {
            when {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showToast(R.string.incorrect_email)
                    return false
                }

                password.length < 6 || password.contains(" ") -> {
                    showToast(R.string.password_length_require)
                    return false
                }

                password != repeatPassword -> {
                    showToast(R.string.password_mismatch)
                    return false
                }

                username.length < 4 || username.contains(" ") -> {
                    showToast(R.string.username_length_require)
                    return false
                }

                !isChecked -> {
                    showToast(R.string.privacy_agreement)
                    return false
                }

                else -> return true
            }
        }

        fun createUser() {
            lifecycleScope.launch {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val name = binding.userNameEditText.text.toString()
                viewModel.registerUser(context(), email, password, name)
            }
        }

        if (checkData()) {
            createUser()
            findNavController().popBackStack()
        }
    }


}