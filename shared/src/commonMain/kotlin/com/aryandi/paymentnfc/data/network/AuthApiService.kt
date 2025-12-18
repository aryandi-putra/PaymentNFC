package com.aryandi.paymentnfc.data.network

import com.aryandi.paymentnfc.data.dto.LoginRequest
import com.aryandi.paymentnfc.data.dto.LoginResponse
import io.ktor.client.HttpClient

class AuthApiService(httpClient: HttpClient) : ApiService(httpClient) {
    
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        val request = LoginRequest(username = username, password = password)
        return post<LoginResponse, LoginRequest>(
            endpoint = "https://paymentnfc.free.beeceptor.com/login",
            body = request
        )
    }
}
