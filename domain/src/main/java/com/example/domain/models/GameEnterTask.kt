package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GameEnterTask(
    var id: Int = 0,
    var task: String? = null,
    var correct_answer: String? = null
) : Parcelable