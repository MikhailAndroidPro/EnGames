package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GameModel(
    var name: String,
    var description: String,
    var name_ru: String,
    var description_ru: String,
    var id: Int
) : Parcelable