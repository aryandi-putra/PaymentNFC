package com.aryandi.paymentnfc.data.network

import com.aryandi.paymentnfc.data.dto.LoginRequest
import com.aryandi.paymentnfc.data.dto.LoginResponse
import com.aryandi.paymentnfc.data.dto.RegisterRequest
import io.ktor.client.HttpClient

interface AuthApiService {
    suspend fun login(username: String, password: String): Result<LoginResponse>
    suspend fun register(request: RegisterRequest): Result<LoginResponse>
}

open class AuthApiServiceImpl(httpClient: HttpClient) : ApiService(httpClient), AuthApiService {

    override suspend fun login(username: String, password: String): Result<LoginResponse> {
        val request = LoginRequest(username = username, password = password)
        return post<LoginResponse, LoginRequest>(
            endpoint = "${ApiConstant.BASE_URL}${ApiConstant.ENDPOINT_LOGIN}",
            body = request
        )
    }

    override suspend fun register(request: RegisterRequest): Result<LoginResponse> {
        return post<LoginResponse, RegisterRequest>(
            endpoint = "${ApiConstant.BASE_URL}${ApiConstant.ENDPOINT_REGISTER}",
            body = request
        )
    }

    override suspend fun register(request: RegisterRequest): Result<LoginResponse> {
        return post<LoginResponse, RegisterRequest>(
            endpoint = "https://paymentnfc.free.beeceptor.com/register",
            body = request
        )
    }
}
