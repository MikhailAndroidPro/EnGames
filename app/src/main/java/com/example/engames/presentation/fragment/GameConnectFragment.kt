package com.example.engames.presentation.fragment

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.GameConnectModel
import com.example.engames.R
import com.example.engames.data.ResponseState
import com.example.engames.databinding.FragmentGameConnectBinding
import com.example.engames.databinding.FragmentProfileBinding
import com.example.engames.presentation.base.BaseViewModel
import com.example.engames.presentation.base.fragment.BaseFragment
import com.example.engames.presentation.viewmodel.GameConnectViewModel
import okhttp3.internal.concurrent.Task

/**
 * Fragment for the "Connect Words" game.
 */
class GameConnectFragment : BaseFragment<FragmentGameConnectBinding>(
    FragmentGameConnectBinding::inflate
) {
    override val viewModel: GameConnectViewModel by viewModels()

    /**
     * Initializes the view and fetches game data.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGame2()
    }
    private var correctConnections = 0

    /**
     * Observes LiveData from the ViewModel.
     */
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
        viewModel.droppedCount.observe(viewLifecycleOwner) { count ->
            if (count == 4) {
                finish()
            }
        }
    }

    /**
     * Sets up drag and drop functionality for word pairs.
     */
    private fun setDragAndDrop() {
        with(binding) {
            for (i in 0 until englishColumn.childCount) {
                val englishView = englishColumn.getChildAt(i)
                val russianView = rusColumn.getChildAt(i)
                val key = englishView.tag?.toString() ?: ""
                setupDragAndDrop(englishView, russianView, key)
            }
        }
    }

    /**
     * Sets up the view with game data.
     */
    private fun setupView(task: GameConnectModel) {
        val matchMap = mutableMapOf<String, String>()
        val pairs = listOf(
            "word0" to (task.word_en0 to task.word_ru0),
            "word1" to (task.word_en1 to task.word_ru1),
            "word2" to (task.word_en2 to task.word_ru2),
            "word3" to (task.word_en3 to task.word_ru3)
        ).shuffled()
        pairs.forEach { (tag, pair) ->
            matchMap[tag] = pair.second.toString()
        }
        viewModel.matchMap = matchMap

        val enWords = pairs.map { it.first to it.second.first }.shuffled()
        val ruWords = pairs.map { it.first to it.second.second }.shuffled()

        with(binding) {
            listOf(en1Txt, en2Txt, en3Txt, en4Txt).forEachIndexed { index, textView ->
                textView.text = enWords[index].second
                textView.tag = enWords[index].first
            }

            listOf(ru1Txt, ru2Txt, ru3Txt, ru4Txt).forEachIndexed { index, textView ->
                textView.text = ruWords[index].second
                textView.tag = ruWords[index].first
            }
        }
        setDragAndDrop()
    }

    /**
     * Sets up drag and drop listeners for a specific pair of views.
     */
    fun setupDragAndDrop(draggableView: View, targetView: View, matchKey: String) {
        draggableView.tag = matchKey
        targetView.tag = matchKey
        draggableView.setOnLongClickListener {
            val dragData = ClipData.newPlainText("word", matchKey)
            val shadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(dragData, shadowBuilder, it, 0)
            true
        }
        targetView.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.alpha = 0.5f
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    v.alpha = 1f
                    true
                }

                DragEvent.ACTION_DROP -> {
                    if (!v.isEnabled) return@setOnDragListener false
                    v.alpha = 1f
                    val draggedView = event.localState as? View ?: return@setOnDragListener false
                    val draggedTag = draggedView.tag?.toString() ?: return@setOnDragListener false
                    val targetText = (v as? TextView)?.text?.toString() ?: return@setOnDragListener false
                    val expectedTranslation = viewModel.matchMap[draggedTag]

                    val isCorrect = expectedTranslation == targetText
                    v.setBackgroundResource(
                        if (isCorrect) R.drawable.right_connect_background
                        else R.drawable.wrong_connect_background
                    )
                    draggedView.visibility = View.INVISIBLE
                    v.isEnabled = false
                    if (isCorrect) correctConnections++
                    viewModel.droppedCount.value = (viewModel.droppedCount.value ?: 0) + 1
                    return@setOnDragListener true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    v.alpha = 1f
                    true
                }

                else -> false
            }
        }
    }

    /**
     * Finishes the game, records results, and shows an end-game dialog.
     */
    private fun finish(){
        viewModel.finish(context(), 2, correctConnections)
        showEndGameDialog(
            buildString {
                append(resources.getString(R.string.you_guess))
                append(" ")
                append(correctConnections)
                append("!")
            },
            if (correctConnections < 4) resources.getString(R.string.try_again) else resources.getString(R.string.keep_up)
        ) {
            findNavController().popBackStack()
        }
    }
}