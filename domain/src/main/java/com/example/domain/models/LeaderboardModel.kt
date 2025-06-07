package com.example.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardModel(
    val rank: Int? = null,
    val name: String? = null,
    val user_rating: Int? = null,
    val photo_link: String? = null
)