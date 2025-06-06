package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GameChoiceTask(
    var id: Int = 0,
    var task: String? = null,
    var answer0: String? = null,
    var answer1: String? = null,
    var answer2: String? = null,
    var answer3: String? = null,
    var correct_answer_id: Int = 0
) : Parcelable