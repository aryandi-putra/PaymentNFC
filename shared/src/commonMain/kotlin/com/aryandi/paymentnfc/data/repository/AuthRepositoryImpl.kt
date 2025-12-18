package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.network.AuthApiService
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApiService: AuthApiService
) : AuthRepository {
    
    override suspend fun login(username: String, password: String): Result<User> {
        return authApiService.login(username, password).mapCatching { response ->
            // Map DTO to domain model
            User(
                id = response.id,
                username = response.username,
                email = response.email,
                firstName = response.firstName,
                lastName = response.lastName,
                gender = response.gender,
                image = response.image,
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        }
    }
}
