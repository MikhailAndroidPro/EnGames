package com.example.engames.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.models.GameEnterTask
import com.example.engames.presentation.base.BaseViewModel

class GameEnterViewModel : BaseViewModel() {
    private val _task = MutableLiveData<GameEnterTask>()
    val task: LiveData<GameEnterTask> get() = _task

    override fun resumeState() {

    }
    fun win(){

    }
    fun lose(){

    }
}