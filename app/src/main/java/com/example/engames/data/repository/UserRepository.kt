package com.example.engames.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.domain.models.FullStatistic
import com.example.domain.models.FullUser
import com.example.domain.models.LeaderboardModel
import com.example.domain.models.UserProfile
import com.example.domain.models.UserSettings
import com.example.domain.models.UserStatistic
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.reflect.full.memberProperties

/** Manages user data interactions with Supabase. */
class UserRepository(private val supabase: SupabaseClient) {

    /** Fetches user profile information by UUID. */
    suspend fun getUserInfo(uuid: String): UserProfile {
        return try {
            val data = supabase.from("User")
                .select {
                    filter {
                        eq("uuid", uuid)
                    }
                }
                .decodeSingle<FullUser>()
            UserProfile(
                data.username,
                data.email,
                "",
                data.gender_id,
                data.photo_link,
                data.password_hash
            )
        } catch (e: Exception) {
            Log.e("User repository", "Failed: ${e.message}")
            return UserProfile()
        }
    }

    /** Fetches user settings (theme and language) by UUID. */
    suspend fun getUserSettings(uuid: String): UserSettings {
        return try {
            val data = supabase.from("User")
                .select {
                    filter {
                        eq("uuid", uuid)
                    }
                }
                .decodeSingle<FullUser>()
            UserSettings(
                data.theme_id,
                data.current_language
            )
        } catch (e: Exception) {
            Log.e("User repository", "Failed: ${e.message}")
            return UserSettings()
        }
    }

    /** Checks if a user is marked as deleted by UUID. */
    suspend fun isUserDeleted(uuid: String): Boolean {
        return try {
            supabase.from("User")
                .select {
                    filter {
                        eq("uuid", uuid)
                    }
                }
                .decodeSingle<FullUser>().is_deleted
        } catch (e: Exception) {
            Log.e("User repository", "Failed: ${e.message}")
            return false
        }
    }

    /** Creates a new user and their initial statistics. */
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
                theme_id = App.settingsManager.getCurrentTheme().id,
                is_deleted = false
            )
            val lastEntrance = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            supabase.from("User").insert(userData)
            supabase.from("Statistic")
                .insert(FullStatistic(last_entrance = lastEntrance, uuid = userData.uuid))
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

    /** Changes the theme and language settings for the current user. */
    suspend fun changeSettings(
        context: Context,
        themeId: Int,
        languageId: Int
    ): ResponseState<Unit> {
        try {
            val uid = App.sharedManager.getUid() ?: return ResponseState.Error("UID not found")
            println("ABOBA " + languageId)
            supabase.from("User")
                .update({
                    set("theme_id", themeId)
                    set("current_language", languageId)
                }) {
                    select()
                    filter {
                        eq("uuid", uid)
                    }
                }
            return ResponseState.Success(Unit)
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

    /** Uploads a photo (from URI) to Supabase storage and returns its public URL. */
    suspend fun sendPhotoToStorage(
        context: Context,
        uri: Uri,
        uuid: String
    ): ResponseState<String> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()

            if (bytes == null) {
                return ResponseState.Error(context.getString(R.string.file_read_error))
            }
            val fileName =
                "${
                    SimpleDateFormat(
                        "HH.mm.dd.MM.yyyy",
                        Locale.getDefault()
                    ).format(Date())
                }$uuid.png"
            val bucket = supabase.storage.from("avatars")
            bucket.upload(path = fileName, data = bytes)
            val publicUrl = bucket.publicUrl(fileName)
            ResponseState.Success(publicUrl)
        } catch (e: RestException) {
            ResponseState.Error(context.getString(R.string.internet_excepteion))
        } catch (e: HttpRequestException) {
            ResponseState.Error(context.getString(R.string.user_not_found))
        } catch (e: HttpRequestTimeoutException) {
            ResponseState.Error(context.getString(R.string.timeout_exception))
        } catch (e: Exception) {
            ResponseState.Error(context.getString(R.string.exception))
        }
    }

    /** Uploads a photo (from Bitmap) to Supabase storage and returns its public URL. */
    suspend fun sendBitmapToStorage(
        context: Context,
        bitmap: Bitmap,
        uuid: String
    ): ResponseState<String> {
        return try {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            stream.close()
            val fileName =
                "${
                    SimpleDateFormat(
                        "HH.mm.dd.MM.yyyy",
                        Locale.getDefault()
                    ).format(Date())
                }$uuid.png"
            val bucket = supabase.storage.from("avatars")
            bucket.upload(
                path = fileName,
                data = byteArray
            )
            val publicUrl = bucket.publicUrl(fileName)
            ResponseState.Success(publicUrl)
        } catch (e: RestException) {
            ResponseState.Error(context.getString(R.string.internet_excepteion))
        } catch (e: HttpRequestException) {
            ResponseState.Error(context.getString(R.string.user_not_found))
        } catch (e: HttpRequestTimeoutException) {
            ResponseState.Error(context.getString(R.string.timeout_exception))
        } catch (e: Exception) {
            ResponseState.Error(context.getString(R.string.exception))
        }
    }

    /** Updates user profile information, including password if provided. */
    suspend fun updateUser(context: Context, user: UserProfile): ResponseState<Unit> {
        try {
            val uid = App.sharedManager.getUid() ?: return ResponseState.Error("UID not found")
            if (user.newPassword.isNotEmpty()) {
                supabase.from("User")
                    .update({
                        set("password_hash", user.newPassword.hashCode())
                    }) {
                        select()
                        filter {
                            eq("uuid", uid)
                        }
                    }
            }
            App.authRepository.updateAuthUser(
                context,
                user.newPassword,
                user.email
                    ?: return ResponseState.Error(context.resources.getResourceName(R.string.exception))
            )
            supabase.from("User")
                .update({
                    set("email", user.email)
                    set("username", user.username)
                    set("gender_id", user.gender)
                    set("photo_link", user.image)
                }) {
                    select()
                    filter {
                        eq("uuid", uid)
                    }
                }
            return ResponseState.Success(Unit)
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

    /** Fetches full user statistics by UUID. */
    suspend fun getUserStatistic(uuid: String): FullStatistic {
        return try {
            supabase.from("Statistic")
                .select {
                    filter {
                        eq("uuid", uuid)
                    }
                }
                .decodeSingle<FullStatistic>()
        } catch (e: Exception) {
            Log.e("User repository", "Failed: ${e.message}")
            return FullStatistic()
        }
    }

    /** Fetches the leaderboard data. */
    suspend fun getLeaderBoard(context: Context): List<LeaderboardModel> {
        return try {
            supabase.postgrest.rpc("get_leaderboard")
                .decodeList<LeaderboardModel>()
        } catch (e: Exception) {
            Log.e("User repository", "Failed: ${e.message}")
            emptyList()
        }
    }

    /** Updates the user's statistics for a specific game. */
    suspend fun updateUserStatistic(
        context: Context,
        gameId: Int,
        points: Int
    ): ResponseState<Unit> {
        try {
            val uid = App.sharedManager.getUid() ?: return ResponseState.Error("UID not found")
            val statTable = supabase.from("Statistic")
            val oldStatistic = statTable.select {
                filter {
                    eq("uuid", uid)
                }
            }.decodeSingle<FullStatistic>()
            val propertyName = "game${gameId + 1}_rating"
            val kClass = FullStatistic::class
            val property = kClass.memberProperties.firstOrNull { it.name == propertyName }
            val oldRating = property?.getter?.call(oldStatistic) as? Int ?: 0
            statTable.update({
                set(propertyName, oldRating + points)
                set(
                    "last_entrance",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                )
            }) {
                select()
                filter {
                    eq("uuid", uid)
                }
            }
            return ResponseState.Success(Unit)
        } catch (e: Exception) {
            return ResponseState.Error(context.getString(R.string.statistics_not_saved))
        }
    }

    /** Updates the user's quiz score, storing the maximum achieved. */
    suspend fun updateUserQuizStatistic(context: Context, points: Int): ResponseState<Unit> {
        try {
            val uid = App.sharedManager.getUid() ?: return ResponseState.Error("UID not found")
            val statTable = supabase.from("Statistic")
            val oldMax = statTable.select {
                filter {
                    eq("uuid", uid)
                }
            }.decodeSingle<FullStatistic>().quiz_score
            val newScore = max(oldMax, points)
            statTable.update({
                set("quiz_score", newScore)
                set(
                    "last_entrance",
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                )
            }) {
                select()
                filter {
                    eq("uuid", uid)
                }
            }
            return ResponseState.Success(Unit)
        } catch (e: Exception) {
            return ResponseState.Error(context.getString(R.string.statistics_not_saved))
        }
    }
}