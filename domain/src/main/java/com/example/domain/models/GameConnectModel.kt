package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class GameConnectModel(
    var id: Int = 0,
    var word_ru0: String? = null,
    var word_ru1: String? = null,
    var word_ru2: String? = null,
    var word_ru3: String? = null,
    var word_en0: String? = null,
    var word_en1: String? = null,
    var word_en2: String? = null,
    var word_en3: String? = null
) : Parcelable
