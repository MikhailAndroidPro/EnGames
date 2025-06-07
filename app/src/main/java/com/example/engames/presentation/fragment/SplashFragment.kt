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

/**
 * SplashFragment class, represents the splash screen of the application.
 */
class SplashFragment : BaseFragment<FragmentSplashBinding>(
    FragmentSplashBinding::inflate
) {
    /**
     * Called when the view is created, checks login status and navigates accordingly.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().setOrientationUnlocked(false)
        // Check if the user is logged in
        if (App.sharedManager.checkLogIn()){
            findNavController().navigate(R.id.action_splashFragment_to_gamesFragment)
        }
        else {
            findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }
}