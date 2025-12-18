package com.aryandi.paymentnfc.data.network

import io.ktor.client.HttpClient

expect object HttpClientFactory {
    fun create(): HttpClient
}