package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.dto.LoginResponse
import com.aryandi.paymentnfc.data.dto.RegisterRequest
import com.aryandi.paymentnfc.data.network.AuthApiService
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApiService: AuthApiService
) : AuthRepository {
    
    override suspend fun login(username: String, password: String): Result<User> {
        return authApiService.login(username, password).mapCatching { it.toDomain() }
    }

    override suspend fun register(
        username: String,
        email: String,
        firstName: String,
        lastName: String
    ): Result<User> {
        val request = RegisterRequest(
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
        return authApiService.register(request).mapCatching { it.toDomain() }
    }

    private fun LoginResponse.toDomain(): User {
        return User(
            id = id,
            username = username,
            email = email,
            firstName = firstName,
            lastName = lastName,
            gender = gender,
            image = image,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
