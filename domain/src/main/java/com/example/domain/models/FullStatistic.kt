package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class FullStatistic(
    val id: Int = 0,
    val last_entrance: String?= "",
    val quiz_score: Int = 0,
    val user_rating: Int = 0,
    val game1_rating: Int = 0,
    val game2_rating: Int = 0,
    val game3_rating: Int = 0,
    val game4_rating: Int = 0,
    var uuid: String? = ""
) : Parcelable