package com.example.domain.models

import com.example.domain.models.enums.Gender

data class UserProfile(
    var username: String? = null,
    var email: String? = null,
    var newPassword: String = "",
    var gender: Gender = Gender.Male,
    var image: String? = null
)
