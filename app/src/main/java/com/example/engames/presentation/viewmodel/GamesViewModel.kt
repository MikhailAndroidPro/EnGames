package com.example.engames.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.GameModel
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch


class GamesViewModel : BaseViewModel() {
    private val _listGames = MutableLiveData<ArrayList<GameModel>>(arrayListOf())
    val listGames: LiveData<ArrayList<GameModel>> get() = _listGames
    var isTaskReady: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun loadGames() {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {
                Log.e("Supabase", "Error fetching table list", e)
            } finally {
                isTaskReady.value = true
            }
        }
    }

    override fun resumeState() {

    }
}