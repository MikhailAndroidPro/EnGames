package com.example.engames.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameModel
import com.example.engames.databinding.GameViewBinding
import com.example.engames.presentation.base.adapter.BaseRecyclerAdapter

class GamesAdapter(
    list: List<GameModel>,
    private val onClickListener: OnClickListener,
    private val isRussian: Boolean
) : BaseRecyclerAdapter<GameModel, GameViewBinding>(list, GameViewBinding::inflate) {

    interface OnClickListener{
        fun click(position: Int)
    }

    override fun onBind(binding: GameViewBinding, item: GameModel, position: Int) {
        binding.name.text = if (isRussian) item.name_ru else item.name
        binding.description.text = if (isRussian) item.description_ru else item.description
        binding.root.setOnClickListener {
            onClickListener.click(position)
        }
    }
}