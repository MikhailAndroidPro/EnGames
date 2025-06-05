package com.example.engames.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.models.GameModel
import com.example.engames.presentation.base.BaseViewModel


class GamesViewModel : BaseViewModel() {
    private val _listGames = MutableLiveData<ArrayList<GameModel>>(arrayListOf())
    val listGames: LiveData<ArrayList<GameModel>> get() = _listGames
    var isTaskReady: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    override fun resumeState() {

    }
}