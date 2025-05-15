package com.example.engames.presentation.base

import android.os.Bundle
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private var _info: Bundle = Bundle()
    val info get() = _info

    fun saveState(bundle: Bundle) {
        this._info = bundle
    }
    abstract fun resumeState()
}