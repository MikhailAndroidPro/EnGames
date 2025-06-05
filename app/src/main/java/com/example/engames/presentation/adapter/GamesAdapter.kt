package com.example.engames.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.GameModel
import com.example.engames.databinding.GameViewBinding
import com.example.engames.presentation.base.adapter.BaseRecyclerAdapter

class GamesAdapter(
    list: List<GameModel>
) : BaseRecyclerAdapter<GameModel, GameViewBinding>(list, GameViewBinding::inflate) {

    override fun onBind(binding: GameViewBinding, item: GameModel) {
        binding.name.text = item.name
        binding.description.text = item.description
    }
}