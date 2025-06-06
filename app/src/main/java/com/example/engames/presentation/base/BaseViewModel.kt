package com.example.engames.presentation.base

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.engames.data.ResponseState

abstract class BaseViewModel : ViewModel() {
    var isTaskReady: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    protected val _state = MutableLiveData<ResponseState<Unit>>()
    val state: LiveData<ResponseState<Unit>> = _state

    abstract fun resumeState()
}