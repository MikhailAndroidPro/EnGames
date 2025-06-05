package com.example.engames.presentation.base

import android.os.Bundle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    abstract fun resumeState()
}