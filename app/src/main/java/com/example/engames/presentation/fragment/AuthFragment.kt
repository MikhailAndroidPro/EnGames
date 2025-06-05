package com.example.engames.presentation.fragment

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.databinding.FragmentAuthBinding
import com.example.engames.presentation.base.fragment.BaseFragment


class AuthFragment : BaseFragment<FragmentAuthBinding>(
    FragmentAuthBinding::inflate
) {
    private var isPasswordHidden = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().hideNavigationView()
    }
    override fun applyClick() {
        super.applyClick()
        binding.eyeBtn.setOnClickListener{
            if (isPasswordHidden) {
                binding.eyeBtn.setImageDrawable(context().getDrawable(R.drawable.eye_open))
                binding.passwordEditText.transformationMethod = null
            } else {
                binding.eyeBtn.setImageDrawable(context().getDrawable(R.drawable.eye_close))
                binding.passwordEditText.transformationMethod = PasswordTransformationMethod()
            }
            isPasswordHidden = !isPasswordHidden
        }
        binding.signUpText.setOnClickListener{
            findNavController().navigate(R.id.action_authFragment_to_regFragment)
        }
    }
    private fun authUser(){
        App.sharedManager.logIn()
    }
}