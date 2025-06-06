package com.example.engames.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.GameEnterTask
import com.example.engames.R
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentGameChoiceBinding
import com.example.engames.databinding.FragmentGameEnterBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GameEnterViewModel

class GameEnterFragment : BaseFragment<FragmentGameEnterBinding>(
    FragmentGameEnterBinding::inflate
) {
    override val viewModel: GameEnterViewModel by viewModels()
    private lateinit var correctAnswer: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGame3()
    }

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
                .lowercase() == correctAnswer
        ) {
            win()
        }
        else lose()
    }

    private fun win(){
        viewModel.win()
        showToast(resources.getString(R.string.you_won))
        findNavController().popBackStack()
    }
    private fun lose(){
        viewModel.lose()
        showToast(buildString {
            append(getString(R.string.you_wrong))
            append(getString(R.string.correct_word_is))
            append(correctAnswer)
        })
        findNavController().popBackStack()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.task.observe(viewLifecycleOwner) { task ->
            when (task) {
                is ResponseState.Success -> {
                    setupView(task.data)
                }

                is ResponseState.Error -> {
                    showToast(task.message)
                }
            }
        }
    }

    private fun setupView(data: GameEnterTask) {
        with(binding) {
            correctAnswer = data.correct_answer.toString()
            data.let {
                questionTxt.text = data.task.toString()
            }
        }
    }
}