package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.UseCase
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) : UseCase<LoginUseCase.Params, User>() {
    
    override suspend fun invoke(params: Params): Result<User> {
        if (params.username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }
        if (params.password.isBlank()) {
            return Result.failure(IllegalArgumentException("Password cannot be empty"))
        }
        
        return authRepository.login(params.username, params.password)
    }
    
    data class Params(
        val username: String,
        val password: String
    )
}
