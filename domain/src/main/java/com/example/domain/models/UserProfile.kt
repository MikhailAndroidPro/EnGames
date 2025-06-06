package com.example.domain.models

import com.example.domain.models.enums.Gender

data class UserProfile(
    var username: String? = null,
    var email: String? = null,
    var newPassword: String = "",
    var gender: Int = Gender.Male.ordinal,
    var image: String? = null
)
