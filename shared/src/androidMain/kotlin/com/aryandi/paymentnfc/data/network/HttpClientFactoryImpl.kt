package com.aryandi.paymentnfc.data.network

import com.aryandi.paymentnfc.logging.EmptyKtorLogger
import com.aryandi.paymentnfc.logging.KermitKtorLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual object HttpClientFactory {
    actual fun create(
        enableNetworkLogs: Boolean,
    ): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                })
            }
            
            // Use Ktor's logging plugin with Kermit for basic logging
            install(Logging) {
                logger = if (enableNetworkLogs) KermitKtorLogger() else EmptyKtorLogger
                level = if (enableNetworkLogs) LogLevel.ALL else LogLevel.NONE
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }
    }
}