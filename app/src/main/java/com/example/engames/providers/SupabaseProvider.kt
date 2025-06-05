package com.example.engames.providers

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

object SupabaseProvider {
    private const val URL = "URL"
    private const val KEY = "KEY"

    @OptIn(SupabaseInternal::class)
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseKey = KEY,
            supabaseUrl = URL
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
            install(Realtime)
            httpConfig {
                Logging { level = LogLevel.BODY }
            }
        }
    }
}