package com.example.engames.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.models.GameChoiceTask
import com.example.domain.models.GameModel
import com.example.engames.presentation.base.BaseViewModel

class GameChoiceViewModel : BaseViewModel() {
    private val _task = MutableLiveData<GameChoiceTask>()
    val task: LiveData<GameChoiceTask> get() = _task

    override fun resumeState() {

    }
    fun win(){

    }
    fun lose(){

    }
}