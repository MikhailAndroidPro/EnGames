package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import com.example.engames.databinding.FragmentGamesBinding
import com.example.engames.presentation.base.fragment.BaseFragment

class GameVictorineFragment : BaseFragment<FragmentGamesBinding>(
    FragmentGamesBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity().showNavigationView()
    }
}