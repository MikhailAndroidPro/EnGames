package com.example.engames.data.repository

import android.util.Log
import com.example.domain.models.UserProfile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class UserRepository(private val supabase: SupabaseClient) {
    suspend fun getUserInfo(uuid: String): UserProfile {
        return try {
            val data = supabase.from("Users")
                .select {
                    filter {
                        eq("user_id", uuid)
                    }
                }
                .decodeSingle<UserProfile>()
            data
        } catch (e: Exception) {
            Log.e("User repository", "Failed: ${e.message}")
            return UserProfile()
        }
    }

    suspend fun createUser(uuid: String, username: String, passwordHash: Int) {

    }
    suspend fun changeSettings(themeId: Int, languageId: Int) {

    }

    suspend fun updateUser(user: UserProfile) {

    }

    suspend fun getUserStatistic(uuid: String) {

    }
    suspend fun updateUserStatistic(uuid: String) {

    }
}