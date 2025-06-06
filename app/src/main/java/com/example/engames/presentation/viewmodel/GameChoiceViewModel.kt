package com.example.engames.presentation.viewmodel

import androidx.core.util.Pools
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameChoiceTask
import com.example.domain.models.GameModel
import com.example.domain.models.enums.Difficulty
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import java.text.FieldPosition

class GameChoiceViewModel : BaseViewModel() {
    private val _task = MutableLiveData<ResponseState<GameChoiceTask>>()
    val task: LiveData<ResponseState<GameChoiceTask>> get() = _task

    fun getGame(position: Int){
        when (position) {
            0 -> getGame1()
            else -> getGame4()
        }
    }

    private fun getGame1() {
        viewModelScope.launch {
            try {
                val request = App.gamesRepository.getGame1(Difficulty.Easy)
                _task.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _task.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    private fun getGame4() {
        viewModelScope.launch {
            try {
                val request = App.gamesRepository.getGame4(Difficulty.Easy)
                _task.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _task.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    override fun resumeState() {

    }
    fun win(){

    }
    fun lose(){

    }
}