package com.example.engames.data.repository

import android.content.Context
import com.example.domain.models.GameChoiceTask
import com.example.domain.models.GameConnectModel
import com.example.domain.models.GameModel
import com.example.domain.models.enums.Difficulty
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlin.random.Random

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

    suspend fun getGame1(diff: Difficulty) : GameChoiceTask {
        return try {
            val randomId = when(diff) {
                Difficulty.Easy -> Random.nextInt(1, 11)
                Difficulty.Medium -> Random.nextInt(11, 21)
                Difficulty.Hard -> Random.nextInt(21, 31)
            }

            val data = supabase.from("Game1")
                .select {
                    filter {
                        eq("id", randomId)
                    }
                }.decodeSingle<GameChoiceTask>()
            data
        } catch (e: Exception) {
            return GameChoiceTask()
        }
    }

    suspend fun getGame2(diff: Difficulty) : GameConnectModel {
        return try {
            val randomId = when(diff) {
                Difficulty.Easy -> Random.nextInt(1, 11)
                Difficulty.Medium -> Random.nextInt(11, 21)
                Difficulty.Hard -> Random.nextInt(21, 31)
            }

            val data = supabase.from("Game2")
                .select {
                    filter {
                        eq("id", randomId)
                    }
                }.decodeSingle<GameConnectModel>()
            data
        } catch (e: Exception) {
            return GameConnectModel()
        }
    }

    suspend fun getGame3(diff: Difficulty) {

    }

    suspend fun getGame4(diff: Difficulty) : GameChoiceTask {
        return try {
            val randomId = when(diff) {
                Difficulty.Easy -> Random.nextInt(1, 16)
                Difficulty.Medium -> Random.nextInt(16, 31)
                Difficulty.Hard -> Random.nextInt(31, 46)
            }

            val data = supabase.from("Game4")
                .select {
                    filter {
                        eq("id", randomId)
                    }
                }.decodeSingle<GameChoiceTask>()
            data
        } catch (e: Exception) {
            return GameChoiceTask()
        }
    }

    suspend fun getGame5(diff: Difficulty) {

    }
}