package com.aryandi.paymentnfc.domain.repository

import com.aryandi.paymentnfc.domain.model.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun register(
        username: String,
        email: String,
        firstName: String,
        lastName: String
    ): Result<User>

    suspend fun verifyOtp(emailOrPhone: String, otp: String): Result<User>
    suspend fun resendOtp(emailOrPhone: String): Result<Unit>
}
