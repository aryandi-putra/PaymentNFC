package com.aryandi.paymentnfc.di

import com.aryandi.paymentnfc.data.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> { HttpClientFactory.create() }
}