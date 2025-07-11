package com.example.engames.presentation.fragment

import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentRegBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.RegViewModel
import kotlinx.coroutines.launch

// RegFragment is a fragment for user registration.
class RegFragment : BaseFragment<FragmentRegBinding>(
    FragmentRegBinding::inflate
) {
    private var isPasswordHidden = true
    private var isRepeatPasswordHidden = true
    // viewModel provides data for the fragment.
    override val viewModel: RegViewModel by viewModels()

    /**
     * applyClick sets click listeners for UI elements.
     */
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

    /**
     * registerUser handles user registration logic.
     */
    private fun registerUser() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val repeatPassword = binding.repeatPasswordEditText.text.toString()
        val username = binding.userNameEditText.text.toString()
        val isChecked = binding.checkboxAgreeTermsConditions.isChecked

        /**
         * checkData validates user input.
         */
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

                username.length < 4 || username.replace(" ", "").length <= 3 -> {
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

        /**
         * createUser creates a new user account.
         */
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
        }
    }

    /**
     * setObservers observes LiveData objects for changes.
     */
    override fun setObservers() {
        super.setObservers()

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {}

                is ResponseState.Error -> {
                    if (it.message == resources.getString(R.string.account_restored)) findNavController().popBackStack()
                    showToast(it.message)
                }
            }
        }
        viewModel.stateCreation.observe(viewLifecycleOwner) {
            when(it) {
                is ResponseState.Success -> {
                    showToast(resources.getString(R.string.successfully_registered))
                    findNavController().popBackStack()
                }

                is ResponseState.Error -> {
                    showToast(buildString {
                        append(it.message)
                        append(resources.getString(R.string.of_creation))
                    })
                }
            }
        }
    }

}