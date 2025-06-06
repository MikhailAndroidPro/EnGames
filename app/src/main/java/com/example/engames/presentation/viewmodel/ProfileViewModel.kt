package com.example.engames.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.UserProfile
import com.example.domain.models.enums.Gender
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel(){
    private val _user = MutableLiveData<ResponseState<UserProfile>>()
    val user: LiveData<ResponseState<UserProfile>> get() = _user

    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                val request = App.userRepository.getUserInfo(App.sharedManager.getUid() ?: "")
                _user.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _user.value = ResponseState.Error(e.message.toString())
            }
        }
    }
    fun saveUser(user: UserProfile){

    }
    override fun resumeState() {

    }
}