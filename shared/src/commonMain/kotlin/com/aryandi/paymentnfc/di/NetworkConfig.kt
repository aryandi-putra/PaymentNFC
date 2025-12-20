package com.aryandi.paymentnfc.di

/**
 * Platform-provided configuration for networking.
 *
 * Example:
 * - Debug builds: enableNetworkLogs = true
 * - Release builds: enableNetworkLogs = false
 */
data class NetworkConfig(
    val enableNetworkLogs: Boolean = false,
)
