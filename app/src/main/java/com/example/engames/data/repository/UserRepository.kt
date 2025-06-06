package com.example.engames.data.repository

import android.content.Context
import android.util.Log
import com.example.domain.models.FullStatistic
import com.example.domain.models.FullUser
import com.example.domain.models.UserProfile
import com.example.domain.models.UserStatistic
import com.example.engames.R
import com.example.engames.data.ResponseState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserRepository(private val supabase: SupabaseClient) {
    suspend fun getUserInfo(uuid: String): UserProfile {
        return try {
            val data = supabase.from("User")
                .select {
                    filter {
                        eq("user_id", uuid)
                    }
                }
                .decodeSingle<FullUser>()
            UserProfile(data.username, data.email, "", data.gender_id, data.photo_link)
        } catch (e: Exception) {
            Log.e("User repository", "Failed: ${e.message}")
            return UserProfile()
        }
    }

    suspend fun createUser(
        context: Context,
        uuid: String,
        email: String,
        username: String,
        passwordHash: Int
    ): ResponseState<Unit> {
        return try {
            val userData = FullUser(
                uuid = uuid,
                email = email,
                username = username,
                password_hash = passwordHash,
                theme_id = 2,
                is_deleted = false
            )
            val lastEntrance = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            supabase.from("User").insert(userData)
            val statistic =
                supabase.from("Statistic").insert(FullStatistic(last_entrance = lastEntrance)) {
                    select()
                }.decodeSingle<FullStatistic>()
            val statId = statistic.id
            supabase.from("UserStatistic").insert(UserStatistic(statistic_id = statId, uuid = uuid))
            ResponseState.Success(Unit)
        } catch (e: RestException) {
            return ResponseState.Error(context.resources.getResourceName(R.string.internet_excepteion))
        } catch (e: HttpRequestException) {
            return ResponseState.Error(context.resources.getResourceName(R.string.user_not_found))
        } catch (e: HttpRequestTimeoutException) {
            return ResponseState.Error(context.resources.getResourceName(R.string.timeout_exception))
        } catch (e: Exception) {
            return ResponseState.Error(context.resources.getResourceName(R.string.exception))
        }
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