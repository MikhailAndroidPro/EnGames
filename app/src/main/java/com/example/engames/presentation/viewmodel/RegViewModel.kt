package com.example.engames.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.data.repository.UserRepository
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlin.toString

class RegViewModel : BaseViewModel() {
    private val _stateCreation = MutableLiveData<ResponseState<Unit>>()
    val stateCreation: LiveData<ResponseState<Unit>> = _stateCreation

    /**
     * Creates a new user in the database.
     */
    private fun createUser(context: Context, email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val request = App.userRepository.createUser(
                    context,
                    email = email,
                    passwordHash = password.hashCode(),
                    username = name,
                    uuid = App.sharedManager.getUid() ?: ""
                )
                if (request is ResponseState.Success) {
                    _stateCreation.value = ResponseState.Success(Unit)
                } else {
                    _stateCreation.value = ResponseState.Error(
                        context.resources.getText(R.string.exception).toString()
                    )
                }
            } catch (e: Exception) {
                Log.e("Supabase create error", e.message.toString())
            }
        }
    }

    /**
     * Registers a new user with email, password, and name.
     */
    fun registerUser(context: Context, email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                val request = App.authRepository.signUpUser(context, email, password)
                when (request) {
                    is ResponseState.Success -> {
                        _state.value = ResponseState.Success(Unit)
                        createUser(context, email, password, name)
                    }
                    is ResponseState.Error -> {
                        _state.value = ResponseState.Error(request.message)
                    }
                }
            } catch (e: Exception) {
                Log.e("Supabase create error", e.message.toString())
                _state.value = ResponseState.Error(context.getString(R.string.exception))
            }
        }
    }

    /**
     * Resumes the state of the ViewModel.
     */
    override fun resumeState() {
    }
}