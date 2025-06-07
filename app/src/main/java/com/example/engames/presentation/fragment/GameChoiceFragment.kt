package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.GameChoiceTask
import com.example.engames.R
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentGameChoiceBinding
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GameChoiceViewModel

class GameChoiceFragment : BaseFragment<FragmentGameChoiceBinding>(
    FragmentGameChoiceBinding::inflate
) {
    override val viewModel: GameChoiceViewModel by viewModels()
    private lateinit var currentTask: GameChoiceTask
    private var position: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextViewToggleGroup()
        position = arguments?.getInt("gamePosition")!!
        viewModel.getGame(position)
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

    override fun applyClick() {
        super.applyClick()
        with(binding) {
            answerButton.setOnClickListener {
                if (currentTask.correct_answer_id == getSelectedOption()) {
                    win()
                } else lose()
            }
        }
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
        viewModel.task.observe(viewLifecycleOwner) { task ->
            when (task) {
                is ResponseState.Success -> {}

                is ResponseState.Error -> {
                    showToast(task.message)
                }
            }
        }
    }

    private fun setupView(data: GameChoiceTask) {
        with(binding) {
            currentTask = data
            val gameTask =
                if (position == 0) resources.getString(R.string.select_translation)
                else resources.getString(
                    R.string.select_wrong
                )
            data.let {
                questionTxt.text = buildString {
                    append(gameTask)
                    append(it.task?.replaceFirstChar { char -> char.uppercase() })
                }
                optionAText.text = it.answer0
                optionBText.text = it.answer1
                optionCText.text = it.answer2
                optionDText.text = it.answer3
            }
        }
    }

    fun win() {
        viewModel.win(context(), position, 2)
        showEndGameDialog(
            resources.getString(R.string.you_won),
            resources.getString(R.string.keep_up)
        ) {
            findNavController().popBackStack()
        }
    }

    fun lose() {
        showEndGameDialog(
            resources.getString(R.string.you_lose),
            resources.getString(R.string.try_again)
        ) {
            findNavController().popBackStack()
        }
    }
}