package com.example.engames.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.engames.databinding.FragmentGameChoiceBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GameChoiceViewModel

class GameChoiceFragment : BaseFragment<FragmentGameChoiceBinding>(
    FragmentGameChoiceBinding::inflate
) {
    override val viewModel: GameChoiceViewModel by viewModels()
    private var position: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTextViewToggleGroup()
        position = arguments?.getInt("gamePosition")!!
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
                if (viewModel.task.value?.correctAnswerId == getSelectedOption()){
                    win()
                }
                else lose()
            }
        }
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.isTaskReady.observe(viewLifecycleOwner) { it ->
            if (it) {
                setupView()
            }
        }
    }

    private fun setupView() {
        with(binding) {
            viewModel.task.value?.let {
                questionTxt.text = it.question
                optionAText.text = it.answer1
                optionBText.text = it.answer2
                optionCText.text = it.answer3
                optionDText.text = it.answer4
            }
        }
    }

    fun win() {
        viewModel.win()
        findNavController().popBackStack()
    }
    fun lose() {
        viewModel.lose()
        findNavController().popBackStack()
    }
}