package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.UseCase
import com.aryandi.paymentnfc.domain.repository.AuthRepository

open class ResendOtpUseCase(
    private val authRepository: AuthRepository
) : UseCase<String, Unit>() {
    
    override suspend fun invoke(params: String): Result<Unit> {
        return authRepository.resendOtp(params)
    }
}
