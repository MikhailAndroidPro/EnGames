// Defines a base class for ViewModels.
package com.example.engames.presentation.base

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.engames.data.ResponseState

// Base class for all ViewModels in the application.
// It provides common functionality like managing response state.
abstract class BaseViewModel : ViewModel() {
    protected val _state = MutableLiveData<ResponseState<Unit>>()
    val state: LiveData<ResponseState<Unit>> = _state

    // Abstract function to be implemented by subclasses.
    // Likely used to reset or re-initialize the ViewModel's state.
    abstract fun resumeState()
}