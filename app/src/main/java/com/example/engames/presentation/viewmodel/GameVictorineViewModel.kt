package com.example.engames.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.models.GameChoiceTask
import com.example.domain.models.GameModel
import com.example.engames.presentation.base.BaseViewModel

class GameVictorineViewModel : BaseViewModel() {
    private val _listTask = MutableLiveData<ArrayList<GameChoiceTask>>()
    val listTask: LiveData<ArrayList<GameChoiceTask>> get() = _listTask
    private val _currentQuestionId = MutableLiveData<Int>(0)
    val currentQuestionId: LiveData<Int> get() = _currentQuestionId

    override fun resumeState() {

    }

    fun finish(){
        isTaskReady.value = true
    }

    fun nextQuestion() {
        if (_currentQuestionId.value?.plus(1)) {
            finish()
        }
        else _currentQuestionId.value = _currentQuestionId.value!!+1
    }
}