package com.example.engames.data

// Represents the state of a response, either success or error.
sealed class ResponseState<T> {
    // Represents a successful response with data.
    class Success<T>(val data: T): ResponseState<T>()

    // Represents an error response with an error message.
    class Error<T>(val message: String): ResponseState<T>()
}