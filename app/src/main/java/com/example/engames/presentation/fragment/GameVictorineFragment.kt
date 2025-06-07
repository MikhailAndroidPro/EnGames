package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.GameChoiceTask
import com.example.engames.R
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentGameVictorineBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GameVictorineViewModel

class GameVictorineFragment : BaseFragment<FragmentGameVictorineBinding>(
    FragmentGameVictorineBinding::inflate
) {
    override val viewModel: GameVictorineViewModel by viewModels()
    private var correctAnswers = 0
    private var listQuestions: List<GameChoiceTask> = listOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextViewToggleGroup()
        viewModel.getGame5()
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

        viewModel.listTask.observe(viewLifecycleOwner) { task ->
            when (task) {
                is ResponseState.Success -> {
                    if (task.data.isEmpty() == false) {
                        listQuestions = task.data
                        setupView(0)
                        viewModel.currentQuestionId.observe(viewLifecycleOwner) {
                            setupView(it)
                        }
                    } else {
                        emptyListError()
                    }
                }

                is ResponseState.Error -> {
                    showToast(task.message)
                }
            }

        }
    }

    private fun emptyListError() {

    }

    private fun setupView(id: Int) {
        with(binding) {
            listQuestions.let {
                val task = it[id]
                questionTxt.text = task.task
                optionAText.text = task.answer0
                optionBText.text = task.answer1
                optionCText.text = task.answer2
                optionDText.text = task.answer3
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
                if (listQuestions[viewModel.currentQuestionId.value
                        ?: 0].correct_answer_id == getSelectedOption()
                ) {
                    correctAnswers++
                }
                if (viewModel.nextQuestion(context(), listQuestions.size, correctAnswers)) finish()
            }
        }
    }

    private fun finish() {
        viewModel.finish(context(), correctAnswers)
        showEndGameDialog(
            buildString {
                append(resources.getString(R.string.you_guess))
                append(" ")
                append(correctAnswers)
                append("!")
            },
            resources.getString(R.string.wait_for_record)
        ) {
            findNavController().popBackStack()
        }
    }
}