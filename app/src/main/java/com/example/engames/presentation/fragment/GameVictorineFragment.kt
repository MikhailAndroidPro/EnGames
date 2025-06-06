package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.engames.databinding.FragmentGameVictorineBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GameVictorineViewModel

class GameVictorineFragment : BaseFragment<FragmentGameVictorineBinding>(
    FragmentGameVictorineBinding::inflate
) {
    override val viewModel: GameVictorineViewModel by viewModels()
    private var correctAnswers = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextViewToggleGroup()
    }

    private fun setupTextViewToggleGroup() {
        val buttons = listOf(
            binding.optionA,
            binding.optionB,
            binding.optionC,
            binding.optionD
        )

        buttons.forEach { view ->
            view.setOnClickListener {
                buttons.forEach { it.isSelected = false }
                view.isSelected = true
            }
        }
        binding.optionA.isSelected = true
    }

    private fun getSelectedOption(): Int {
        return when {
            binding.optionA.isSelected -> 0
            binding.optionB.isSelected -> 1
            binding.optionC.isSelected -> 2
            binding.optionD.isSelected -> 3
            else -> 4
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.isTaskReady.observe(viewLifecycleOwner) {
            if (it) {
                finish()
            }
        }
        viewModel.currentQuestionId.observe(viewLifecycleOwner) {
            setupView(it)
        }
        viewModel.listTask.observe(viewLifecycleOwner) {
            setupView(0)
        }
    }

    private fun setupView(id: Int) {
        with(binding) {
            viewModel.listTask.value?.let {
                val task = it[id]
                questionTxt.text = task.question
                optionAText.text = task.answer1
                optionBText.text = task.answer2
                optionCText.text = task.answer3
                optionDText.text = task.answer4
                optionA.performClick()

                questionNumTxt.text = buildString {
                    append(id + 1)
                    append("/")
                    append(it.size)
                }
            }
        }
    }

    override fun applyClick() {
        super.applyClick()
        with(binding) {
            answerButton.setOnClickListener {
                if (viewModel.listTask.value?.get(viewModel.currentQuestionId.value)?.correctAnswerId == getSelectedOption()) {
                    correctAnswers++
                }
                viewModel.nextQuestion()
            }
        }
    }

    private fun finish() {

    }
}