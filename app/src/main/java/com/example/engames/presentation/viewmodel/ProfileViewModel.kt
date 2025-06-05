package com.example.engames.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.UserProfile
import com.example.domain.models.enums.Gender
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel(){
    private val _user = MutableLiveData<UserProfile>()
    val user: LiveData<UserProfile> get() = _user

    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                _user.value = UserProfile("", "", "", Gender.Male, "")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error: ${e.message}")
            }
        }
    }
    fun saveUser(){

    }
    override fun resumeState() {

    }
}