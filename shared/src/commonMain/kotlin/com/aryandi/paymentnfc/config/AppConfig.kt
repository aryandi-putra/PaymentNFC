package com.aryandi.paymentnfc.config

data class AppConfig(
    val isDebug: Boolean,
    val enableNetworkLogging: Boolean,
    val environment: Environment
)

enum class Environment {
    DEV, STAGING, PROD
}

object ConfigHolder {
    lateinit var config: AppConfig
}
