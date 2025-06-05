package com.example.domain.models

import com.example.domain.models.enums.Gender

data class UserProfile(
    var username: String,
    var email: String,
    var newPassword: String = "",
    var gender: Gender = Gender.Male,
    var image: String
)
