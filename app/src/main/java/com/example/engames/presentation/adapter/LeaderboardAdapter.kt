package com.example.engames.presentation.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.example.domain.models.LeaderboardModel
import com.example.engames.R
import com.example.engames.databinding.LeaderViewBinding
import com.example.engames.presentation.base.adapter.BaseRecyclerAdapter
import kotlin.math.min

/**
 * Adapter for displaying leaderboard items in a RecyclerView.
 *
 * @property list List of LeaderboardModel objects.
 * @property context Context for accessing resources.
 */
class LeaderboardAdapter(private val list: List<LeaderboardModel>, private val context: Context) :
    BaseRecyclerAdapter<LeaderboardModel, LeaderViewBinding>(list, LeaderViewBinding::inflate) {

    /**
     * Binds data to the views in each item.
     */
    override fun onBind(
        binding: LeaderViewBinding,
        item: LeaderboardModel,
        position: Int
    ) = with(binding) {
        Glide.with(avatarImg)
            .load(item.photo_link)
            .error(R.drawable.user_image_placeholder)
            .into(avatarImg)
        placeTxt.text = position.plus(1).toString()
        usernameTxt.text = item.name
        scoreTxt.text = item.user_rating.toString()
        when (position) {
            0 -> usernameTxt.setTextColor(context.resources.getColor(R.color.yellow))
            1 -> usernameTxt.setTextColor(context.resources.getColor(R.color.silver))
            2 -> usernameTxt.setTextColor(context.resources.getColor(R.color.bronze))
        }
    }

    /**
     * Returns the number of items to display (max 5).
     */
    override fun getItemCount(): Int = min(5, list.size)
}