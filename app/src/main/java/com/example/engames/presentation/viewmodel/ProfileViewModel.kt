package com.example.engames.presentation.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.UserProfile
import com.example.domain.models.enums.Gender
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel(){
    private val _user = MutableLiveData<ResponseState<UserProfile>>()
    val user: LiveData<ResponseState<UserProfile>> get() = _user
    private val _photo = MutableLiveData<ResponseState<String>>()
    val photo: LiveData<ResponseState<String>> get() = _photo
    var selectedImageUri: Uri? = null
    var selectedBitmap: Bitmap? = null

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
    fun saveUser(context: Context, user: UserProfile, isImageChanged: Boolean) {
        viewModelScope.launch {
            try {
                val uid = App.sharedManager.getUid() ?: ""
                if (isImageChanged) {
                    val uploadResult = when {
                        selectedImageUri != null -> App.userRepository.sendPhotoToStorage(context, selectedImageUri!!, uid)
                        selectedBitmap != null -> App.userRepository.sendBitmapToStorage(context, selectedBitmap!!, uid)
                        else -> ResponseState.Error(
                            context.resources.getText(R.string.failed_to_load_photo).toString()
                        )
                    }

                    if (uploadResult is ResponseState.Success) {
                        user.image = uploadResult.data
                        _photo.value = ResponseState.Success(uploadResult.data)
                    }
                    else {
                        _photo.value = ResponseState.Error(
                            context.resources.getText(R.string.failed_to_load_photo).toString()
                        )
                    }
                }
                val request = App.userRepository.updateUser(context, user)
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