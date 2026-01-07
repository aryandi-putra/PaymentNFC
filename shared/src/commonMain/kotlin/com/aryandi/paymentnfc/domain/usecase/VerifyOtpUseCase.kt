package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.UseCase
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository

open class VerifyOtpUseCase(
    private val authRepository: AuthRepository
) : UseCase<VerifyOtpUseCase.Params, User>() {
    
    override suspend fun invoke(params: Params): Result<User> {
        if (params.otp.length < 6) {
            return Result.failure(IllegalArgumentException("OTP must be 6 digits"))
        }
        return authRepository.verifyOtp(params.emailOrPhone, params.otp)
    }
    
    data class Params(
        val emailOrPhone: String,
        val otp: String
    )
}
