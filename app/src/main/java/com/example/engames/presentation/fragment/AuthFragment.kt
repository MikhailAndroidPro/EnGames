package com.example.engames.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.databinding.FragmentAuthBinding
import com.example.engames.presentation.base.fragment.BaseFragment

class AuthFragment : BaseFragment<FragmentAuthBinding>(
    FragmentAuthBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().hideNavigationView()
    }
    private fun authUser(){
        App.sharedManager.logIn()
    }

    override fun applyClick() {
        super.applyClick()

    }
}