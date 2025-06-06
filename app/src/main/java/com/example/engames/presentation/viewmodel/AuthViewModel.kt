package com.example.engames.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class AuthViewModel : BaseViewModel() {
    fun loginUser(context: Context, email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = App.authRepository.loginUser(context, email, password)
                if (request is ResponseState.Success) {
                    _state.value = ResponseState.Success(Unit)
                } else {
                    _state.value = ResponseState.Error(
                        context.resources.getText(R.string.user_not_found).toString()
                    )
                }
            } catch (e: Exception) {
                _state.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    override fun resumeState() {

    }
}