package com.example.engames.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.FullStatistic
import com.example.domain.models.LeaderboardModel
import com.example.domain.models.UserSettings
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import com.example.engames.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class StatisticViewModel : BaseViewModel() {
    private val _leaderboard = MutableLiveData<ResponseState<List<LeaderboardModel>>>()
    val leaderboard: LiveData<ResponseState<List<LeaderboardModel>>> = _leaderboard
    private val _user = MutableLiveData<ResponseState<FullStatistic>>()
    val user: LiveData<ResponseState<FullStatistic>> = _user

    fun getUserStatistic(context: Context){
        viewModelScope.launch {
            try {
                val uid = App.sharedManager.getUid() ?: ""
                val request = App.userRepository.getUserStatistic(uid)
                _user.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _user.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    fun getLeaderBoard(context: Context){
        viewModelScope.launch {
            try {
                val request = App.userRepository.getLeaderBoard(context)
                _leaderboard.value = ResponseState.Success(request)
            } catch (e: Exception) {
                _leaderboard.value = ResponseState.Error(e.message.toString())
            }
        }
    }

    override fun resumeState() {

    }
}