package com.example.engames.presentation.viewmodel

import android.content.Context
import androidx.core.util.Pools
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameChoiceTask
import com.example.domain.models.GameModel
import com.example.domain.models.enums.Difficulty
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import java.text.FieldPosition

class GameChoiceViewModel : BaseViewModel() {
    private val _task = MutableLiveData<ResponseState<GameChoiceTask>>()
    val task: LiveData<ResponseState<GameChoiceTask>> get() = _task

    // Fetches a game based on the provided position.
    fun getGame(position: Int){
        when (position) {
            0 -> getGame1()
            else -> getGame4()
        }
    }
    // Fetches the first game type (Game1) with Easy difficulty.
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
    // Fetches the fourth game type (Game4) with Easy difficulty.
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
    // Updates user statistics after a game win.
    fun win(context: Context, gameId: Int, points: Int){
        viewModelScope.launch {
            try {
                val request = App.userRepository.updateUserStatistic(context, gameId, points)
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
    // Resumes the ViewModel's state (currently empty).
    override fun resumeState() {

    }
}