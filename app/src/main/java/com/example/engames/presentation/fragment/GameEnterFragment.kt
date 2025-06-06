package com.example.engames.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.engames.R
import com.example.engames.databinding.FragmentGameChoiceBinding
import com.example.engames.databinding.FragmentGameEnterBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GameEnterViewModel

class GameEnterFragment : BaseFragment<FragmentGameEnterBinding>(
    FragmentGameEnterBinding::inflate
) {
    override val viewModel: GameEnterViewModel by viewModels()
    override fun applyClick() {
        super.applyClick()
        with(binding) {
            doneBtn.setOnClickListener {
                checkResult()
            }
        }
    }

    private fun checkResult() {
        if (binding.answerEditText.text.toString()
                .lowercase() == viewModel.task.value?.translation?.lowercase()
        ) {
            win()
        }
        else lose()
    }

    private fun win(){
        viewModel.win()
        findNavController().popBackStack()
    }
    private fun lose(){
        viewModel.lose()
        findNavController().popBackStack()
    }
}