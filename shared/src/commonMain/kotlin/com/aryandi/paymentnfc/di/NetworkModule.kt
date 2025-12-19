package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.HttpClientFactory
import com.aryandi.paymentnfc.config.ConfigHolder
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        val config = ConfigHolder.config
        HttpClientFactory.create(
            enableNetworkLogs = config.enableNetworkLogging,
        )
    }
}