package com.example.engames.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameModel
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch


class GamesViewModel : BaseViewModel() {
    private val _listGames = MutableLiveData<ResponseState<List<GameModel>>>()
    val listGames: LiveData<ResponseState<List<GameModel>>> get() = _listGames

    // Loads the list of games from the repository.
    fun loadGames() {
        viewModelScope.launch {
            try {
                val request = App.gamesRepository.getGamesList()
                _listGames.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _listGames.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    // Placeholder for resuming the ViewModel's state.
    override fun resumeState() {

    }
}