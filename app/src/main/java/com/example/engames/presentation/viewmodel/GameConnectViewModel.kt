package com.example.engames.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameConnectModel
import com.example.domain.models.enums.Difficulty
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class GameConnectViewModel : BaseViewModel() {
    private val _task = MutableLiveData<ResponseState<GameConnectModel>>()
    val task: LiveData<ResponseState<GameConnectModel>> get() = _task
    var matchMap = mutableMapOf<String, String>()
    val droppedCount = MutableLiveData(0)

    fun getGame2() {
        viewModelScope.launch {
            try {
                val request = App.gamesRepository.getGame2(Difficulty.Easy)
                _task.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _task.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    override fun resumeState() {

    }

    fun finish(context: Context, gameId: Int, correctAnswers: Int) {
        viewModelScope.launch {
            try {
                val request =
                    App.userRepository.updateUserStatistic(context, gameId, correctAnswers)
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
}