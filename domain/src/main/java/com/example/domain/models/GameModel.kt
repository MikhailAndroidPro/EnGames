package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GameModel(
    var name: String,
    var description: String,
    var id: Int
) : Parcelable