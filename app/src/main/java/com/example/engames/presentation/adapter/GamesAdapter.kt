package com.example.engames.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameModel
import com.example.engames.databinding.GameViewBinding
import com.example.engames.presentation.base.adapter.BaseRecyclerAdapter

/**
 * Adapter for displaying a list of games in a RecyclerView.
 *
 * @property list The list of games to display.
 * @property onClickListener Listener for item click events.
 * @property isRussian Flag to determine if Russian text should be displayed.
 */
class GamesAdapter(
    list: List<GameModel>,
    private val onClickListener: OnClickListener,
    private val isRussian: Boolean
) : BaseRecyclerAdapter<GameModel, GameViewBinding>(list, GameViewBinding::inflate) {

    /**
     * Binds data to the view holder.
     */
    override fun onBind(binding: GameViewBinding, item: GameModel, position: Int) {
        binding.name.text = if (isRussian) item.name_ru else item.name
        binding.description.text = if (isRussian) item.description_ru else item.description
        binding.root.setOnClickListener {
            onClickListener.click(position)
        }
    }

    /**
     * Interface for handling item click events.
     */
    interface OnClickListener{
        /** Called when an item is clicked. */
        fun click(position: Int)
    }
}