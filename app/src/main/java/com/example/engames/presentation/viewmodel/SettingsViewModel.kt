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

class SettingsViewModel : BaseViewModel(){
    private val _stateDelete = MutableLiveData<ResponseState<Unit>>()
    val stateDelete: LiveData<ResponseState<Unit>> = _stateDelete
    fun logout(context: Context) {
        viewModelScope.launch {
            try {
                val request = App.authRepository.logout(context)
                if (request is ResponseState.Success) {
                    _state.value = ResponseState.Success(Unit)
                } else {
                    _state.value = ResponseState.Error(
                        context.resources.getString(R.string.cant_logout).toString()
                    )
                }
            } catch (e: Exception) {
                _state.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    fun delete(context: Context) {
        viewModelScope.launch {
            try {
                val request = App.authRepository.deleteAccount(context)
                if (request is ResponseState.Success) {
                    _stateDelete.value = ResponseState.Success(Unit)
                } else {
                    _stateDelete.value = ResponseState.Error(
                        context.resources.getString(R.string.cant_delete_account).toString()
                    )
                }
            } catch (e: Exception) {
                _stateDelete.value = ResponseState.Error(e.message.toString())
            }
        }
    }
    override fun resumeState() {

    }
}