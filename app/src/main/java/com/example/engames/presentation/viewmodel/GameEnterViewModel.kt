package com.example.engames.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameEnterTask
import com.example.domain.models.enums.Difficulty
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class GameEnterViewModel : BaseViewModel() {
    private val _task = MutableLiveData<ResponseState<GameEnterTask>>()
    val task: LiveData<ResponseState<GameEnterTask>> get() = _task

    /**
     * Fetches game 3 data with easy difficulty.
     */
    fun getGame3() {
        viewModelScope.launch {
            try {
                val request = App.gamesRepository.getGame3(Difficulty.Easy)
                _task.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _task.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    /**
     * Resumes the ViewModel's state.
     */
    override fun resumeState() {

    }
    /**
     * Updates user statistics after a win.
     */
    fun win(context: Context, gameId: Int, points: Int) {
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
}