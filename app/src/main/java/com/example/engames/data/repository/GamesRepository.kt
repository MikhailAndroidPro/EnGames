package com.example.engames.data.repository

import android.content.Context
import com.example.domain.models.GameModel
import com.example.domain.models.UserProfile
import com.example.domain.models.enums.Difficulty
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.ktor.client.plugins.HttpRequestTimeoutException

class GamesRepository(private val supabase: SupabaseClient) {
    suspend fun getGamesList(): List<GameModel> {
        return try {
            val data = supabase.from("Games")
                .select().decodeList<GameModel>()
            data
        } catch (e: Exception) {
            return emptyList()
        }
    }

    suspend fun getGame1(diff: Difficulty) {

    }

    suspend fun getGame2(diff: Difficulty) {

    }

    suspend fun getGame3(diff: Difficulty) {

    }

    suspend fun getGame4(diff: Difficulty) {

    }

    suspend fun getGame5(diff: Difficulty) {

    }
}