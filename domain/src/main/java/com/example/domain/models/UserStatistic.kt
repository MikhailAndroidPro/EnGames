package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class UserStatistic(
    val id: Long = 0,
    var statistic_id: Int,
    var uuid: String
) : Parcelable