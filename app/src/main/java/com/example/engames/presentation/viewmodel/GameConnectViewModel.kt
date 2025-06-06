package com.example.engames.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.models.GameConnectModel
import com.example.engames.presentation.base.BaseViewModel

class GameConnectViewModel : BaseViewModel() {
    private val _task = MutableLiveData<GameConnectModel>()
    val task: LiveData<GameConnectModel> get() = _task
    var matchMap = mutableMapOf<String, String>()
    val droppedCount = MutableLiveData(0)

    override fun resumeState() {

    }
    fun finish(correctAnswers: Int){

    }
}