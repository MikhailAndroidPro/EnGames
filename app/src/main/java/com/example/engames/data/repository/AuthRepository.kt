package com.example.engames.data.repository

import android.content.Context
import com.example.domain.models.FullUser
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
/**
 * Repository class for handling authentication-related operations.
 * @param supabase The Supabase client instance.
 */
class AuthRepository(private val supabase: SupabaseClient) {
    /**
     * Logs in an existing user.
     * @param context The application context.
     * @param email The user's email.
     * @param password The user's password.
     * @return A [ResponseState] indicating the success or failure of the operation.
     */
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
            if (App.userRepository.isUserDeleted(uuid = id)) return ResponseState.Error(
                context.resources.getResourceName(
                    R.string.user_not_found
                )
            )
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
    /**
     * Signs up a new user.
     * @param context The application context.
     * @param email The user's email.
     * @param password The user's password.
     * @return A [ResponseState] indicating the success or failure of the operation.
     */
    suspend fun signUpUser(
        context: Context,
        email: String,
        password: String
    ): ResponseState<Unit> {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            val id = supabase.auth.currentUserOrNull()?.id
                ?: return ResponseState.Error(context.getString(R.string.failed_to_get_user))
            App.sharedManager.saveUid(id)
            ResponseState.Success(Unit)

        } catch (e: RestException) {
            if (e.message?.contains("User already registered", ignoreCase = true) == true) {
                undeleteAccount(context, email)
                return ResponseState.Error(context.getString(R.string.account_restored))
            }
            ResponseState.Error(context.getString(R.string.internet_excepteion))
        } catch (e: HttpRequestException) {
            ResponseState.Error(context.getString(R.string.user_not_found))
        } catch (e: HttpRequestTimeoutException) {
            ResponseState.Error(context.getString(R.string.timeout_exception))
        } catch (e: Exception) {
            ResponseState.Error(context.getString(R.string.exception))
        }
    }
    /**
     * Logs out the current user.
     * @param context The application context.
     * @return A [ResponseState] indicating the success or failure of the operation.
     */
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
    /**
     * Updates the authenticated user's email and/or password.
     * @param context The application context.
     * @param newPassword The new password (if changing).
     * @param newEmail The new email (if changing).
     * @return A [ResponseState] indicating the success or failure of the operation.
     */
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
    /**
     * Undeletes a previously deleted user account.
     * @param context The application context.
     * @param email The email of the account to undelete.
     * @return A [ResponseState] indicating the success or failure of the operation.
     */
    suspend fun undeleteAccount(
        context: Context,
        email: String
    ): ResponseState<Unit> {
        try {
            val userTable = supabase.from("User")
            val uuid = userTable
                .select { filter { eq("email", email) } }
                .decodeSingle<FullUser>().uuid
            userTable.update(mapOf("is_deleted" to false)) {
                filter { eq("uuid", uuid) }
            }
            return ResponseState.Success(Unit)
        } catch (e: Exception) {
            return ResponseState.Error(context.getString(R.string.exception))
        }
    }
    /**
     * Deletes the current user's account.
     * @param context The application context.
     * @return A [ResponseState] indicating the success or failure of the operation.
     */
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