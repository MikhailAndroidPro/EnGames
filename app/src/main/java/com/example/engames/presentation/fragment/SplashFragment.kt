package com.example.engames.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.app.App

class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (App.sharedManager.checkLogIn()){
            findNavController().navigate(R.id.action_splashFragment_to_gamesFragment)
        }
        else {
            findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }
}