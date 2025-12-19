package com.aryandi.paymentnfc.logging

import io.ktor.client.plugins.logging.Logger

/**
 * Custom Ktor Logger implementation that uses KermitLogger
 */
class KermitKtorLogger : Logger {
    override fun log(message: String) {
        KermitLogger.d(message, tag = "Ktor-Client")
    }
}

/**
 * Empty Ktor Logger implementation that doesn't log any messages
 */
object EmptyKtorLogger : Logger {
    override fun log(message: String) {
        // No logging
    }
}