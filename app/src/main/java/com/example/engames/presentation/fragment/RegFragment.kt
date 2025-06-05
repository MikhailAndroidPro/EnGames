package com.example.engames.presentation.fragment

import android.text.method.PasswordTransformationMethod
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.databinding.FragmentRegBinding
import com.example.engames.presentation.base.fragment.BaseFragment


class RegFragment : BaseFragment<FragmentRegBinding>(
    FragmentRegBinding::inflate
) {
    private var isPasswordHidden = true
    private var isRepeatPasswordHidden = true

    override fun applyClick() {
        super.applyClick()
        binding.signInText.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.signUpButton.setOnClickListener({ v ->
            registerUser()
        })
        binding.eyeBtnPassword.setOnClickListener {
            isPasswordHidden = !isPasswordHidden
            binding.eyeBtnPassword.setImageResource(
                if (isPasswordHidden) R.drawable.eye_close else R.drawable.eye_open
            )
            binding.passwordEditText.transformationMethod =
                if (isPasswordHidden) PasswordTransformationMethod.getInstance() else null
            binding.passwordEditText.setSelection(binding.passwordEditText.text.length)
        }

        binding.eyeBtnRepeatPassword.setOnClickListener {
            isRepeatPasswordHidden = !isRepeatPasswordHidden
            binding.eyeBtnRepeatPassword.setImageResource(
                if (isRepeatPasswordHidden) R.drawable.eye_close else R.drawable.eye_open
            )
            binding.repeatPasswordEditText.transformationMethod =
                if (isRepeatPasswordHidden) PasswordTransformationMethod.getInstance() else null
            binding.repeatPasswordEditText.setSelection(binding.repeatPasswordEditText.text.length)
        }
    }

    private fun registerUser(){

    }
}