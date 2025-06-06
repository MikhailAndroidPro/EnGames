package com.example.engames.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.toString

class RegViewModel : BaseViewModel() {
    fun registerUser(context: Context, email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val request = App.authRepository.signUpUser(context, email, password, name)
                if (request is ResponseState.Success) {

                } else {

                }
            } catch (e: Exception) {
                Log.e("Supabase create error", e.message.toString())
            }
        }
    }

    private fun createUser(context: Context, email: String, password: String, name: String) {

    }

    override fun resumeState() {

    }
}