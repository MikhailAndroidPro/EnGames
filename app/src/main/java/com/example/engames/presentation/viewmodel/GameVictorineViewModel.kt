package com.example.engames.presentation.viewmodel

import android.content.Context
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

class GameVictorineViewModel : BaseViewModel() {
    private val _listTask = MutableLiveData<ResponseState<List<GameChoiceTask>>>()
    val listTask: LiveData<ResponseState<List<GameChoiceTask>>> get() = _listTask
    private val _currentQuestionId = MutableLiveData<Int>(0)
    val currentQuestionId: LiveData<Int> get() = _currentQuestionId

    fun getGame5() {
        viewModelScope.launch {
            try {
                val request = App.gamesRepository.getGame5()
                _listTask.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _listTask.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    override fun resumeState() {

    }

    fun finish(context: Context, correctAnswers: Int) {
        viewModelScope.launch {
            try {
                val request =
                    App.userRepository.updateUserQuizStatistic(context, correctAnswers)
                when (request) {
                    is ResponseState.Success -> {
                        _state.value = ResponseState.Success(Unit)
                    }

                    is ResponseState.Error -> {
                        _state.value = ResponseState.Error(request.message)
                    }
                }
            } catch (e: Exception) {
                _state.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    fun nextQuestion(context: Context, max: Int, correctAnswers: Int) : Boolean {
        if (_currentQuestionId.value?.plus(1) == max) {
            finish(context, correctAnswers)
            return true
        }
        else {
            _currentQuestionId.value = _currentQuestionId.value?.plus(1)
            return false
        }
    }
}