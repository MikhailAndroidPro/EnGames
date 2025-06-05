package com.example.engames.presentation.fragment

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.databinding.FragmentSplashBinding
import com.example.engames.presentation.base.fragment.BaseFragment

class SplashFragment : BaseFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (App.sharedManager.checkLogIn()){
            findNavController().navigate(R.id.action_splashFragment_to_gamesFragment)
        }
        else {
            findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }
}