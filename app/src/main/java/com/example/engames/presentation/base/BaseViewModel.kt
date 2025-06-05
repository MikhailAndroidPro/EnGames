package com.example.engames.presentation.base

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    var isTaskReady: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    abstract fun resumeState()
}