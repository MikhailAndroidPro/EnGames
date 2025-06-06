package com.example.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class FullUser(
    val uuid: String,
    val email: String,
    val username: String,
    val photo_link: String? = null,
    val password_hash: Int,
    val gender_id: Int = 1,
    val current_language: Int = 1,
    val theme_id: Int = 1
) : Parcelable