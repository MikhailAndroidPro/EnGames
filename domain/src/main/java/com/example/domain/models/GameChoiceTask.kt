package com.example.domain.models

data class GameChoiceTask(
    var question: String,
    var answer1: String,
    var answer2: String,
    var answer3: String,
    var answer4: String,
    var correctAnswerId: Int
)