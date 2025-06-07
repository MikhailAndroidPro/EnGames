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

/** Fragment for the 'Enter the word' game. */
class GameEnterFragment : BaseFragment<FragmentGameEnterBinding>(
    FragmentGameEnterBinding::inflate
) {
    override val viewModel: GameEnterViewModel by viewModels()
    private lateinit var correctAnswer: String

    /** Sets up the view and fetches game data when the view is created. */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGame3()
    }

    /** Sets up click listeners for UI elements. */
    override fun applyClick() {
        super.applyClick()
        with(binding) {
            // Set click listener for the 'Done' button to check the result.
            doneBtn.setOnClickListener {
                checkResult()
            }
        }
    }

    /** Checks if the entered answer is correct and calls win() or lose() accordingly. */
    private fun checkResult() {
        if (binding.answerEditText.text.toString()
                .lowercase() == correctAnswer
        ) {
            win()
        }
        else lose()
    }

    /** Handles the win scenario, updates ViewModel, and shows an end-game dialog. */
    private fun win() {
        viewModel.win(context(), 1, 2)
        showEndGameDialog(
            resources.getString(R.string.you_won),
            resources.getString(R.string.keep_up)
        ) {
            findNavController().popBackStack()
        }
    }

    /** Handles the lose scenario and shows an end-game dialog with the correct answer. */
    private fun lose() {
        showEndGameDialog(
            resources.getString(R.string.you_wrong),
            buildString {
                append(getString(R.string.correct_word_is))
                append(correctAnswer)
            }
        ) {
            findNavController().popBackStack()
        }
    }

    /** Sets up observers for LiveData from the ViewModel. */
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

    /** Sets up the UI with data from GameEnterTask. */
    private fun setupView(data: GameEnterTask) {
        with(binding) {
            correctAnswer = data.correct_answer.toString()
            data.let {
                questionTxt.text = data.task.toString()
            }
        }
    }
}