package com.example.engames.data.repository

import android.content.Context
import com.example.engames.R
import com.example.engames.app.App
import com.example.engames.data.ResponseState
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository(private val supabase: SupabaseClient) {
    suspend fun loginUser(
        context: Context,
        email: String,
        password: String
    ): ResponseState<Unit> {
        try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            val id = supabase.auth.currentUserOrNull()?.id ?: "Failed to get user"
            App.sharedManager.saveUid(id.toString())
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

    suspend fun signUpUser(
        context: Context,
        email: String,
        password: String
    ): ResponseState<Unit> {
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val id = supabase.auth.currentUserOrNull()?.id
                ?: context.resources.getString(R.string.failed_to_get_user)
            App.sharedManager.saveUid(id.toString())

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

    suspend fun logout(
        context: Context,
    ): ResponseState<Unit> {
        try {
            supabase.auth.signOut()
            App.sharedManager.saveUid("")
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

    suspend fun updateAuthUser(
        context: Context,
        newPassword: String,
        newEmail: String
    ): ResponseState<Unit> {
        try {
            supabase.auth.updateUser {
                email = newEmail
                if (newPassword.length >= 6) password = newPassword
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

    suspend fun deleteAccount(
        context: Context,
    ): ResponseState<Unit> {
        try {
            val uid = App.sharedManager.getUid() ?: return ResponseState.Error("UID not found")
            supabase.from("User")
                .update(mapOf("is_deleted" to true)) {
                    select()
                    filter {
                        eq("uuid", uid)
                    }
                }
            supabase.auth.signOut()
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
}