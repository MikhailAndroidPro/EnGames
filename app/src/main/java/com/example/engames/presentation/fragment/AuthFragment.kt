package com.example.engames.presentation.fragment

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentAuthBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.AuthViewModel


class AuthFragment : BaseFragment<FragmentAuthBinding>(
    FragmentAuthBinding::inflate
) {
    private var isPasswordHidden = true
    override val viewModel: AuthViewModel by viewModels()

    override fun applyClick() {
        super.applyClick()
        with(binding) {
            eyeBtn.setOnClickListener {
                isPasswordHidden = !isPasswordHidden
                eyeBtn.setImageResource(
                    if (isPasswordHidden) R.drawable.eye_close else R.drawable.eye_open
                )
                passwordEditText.transformationMethod =
                    if (isPasswordHidden) PasswordTransformationMethod.getInstance() else null
                passwordEditText.setSelection(passwordEditText.text.length)
            }
            signUpText.setOnClickListener {
                findNavController().navigate(R.id.action_authFragment_to_regFragment)
            }
            signInButton.setOnClickListener {
                authUser()
            }
        }
    }

    private fun authUser() {
        if (binding.checkboxRememberMe.isChecked) App.sharedManager.logIn()
        viewModel.loginUser(
            context(),
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseState.Success -> {
                    showToast(resources.getString(R.string.successfully_authorized))
                    findNavController ().navigate(R.id.action_authFragment_to_gamesFragment)
                }

                is ResponseState.Error -> {
                    showToast(it.message)
                }
            }
        }
    }
}