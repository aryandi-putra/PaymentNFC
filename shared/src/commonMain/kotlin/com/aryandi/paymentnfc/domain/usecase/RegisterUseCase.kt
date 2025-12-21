package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.UseCase
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository

open class RegisterUseCase(
    private val authRepository: AuthRepository
) : UseCase<RegisterUseCase.Params, User>() {
    
    override suspend fun invoke(params: Params): Result<User> {
        if (params.username.isBlank()) {
            return Result.failure(IllegalArgumentException("Username cannot be empty"))
        }
        if (params.email.isBlank()) {
            return Result.failure(IllegalArgumentException("Email cannot be empty"))
        }
        
        return authRepository.register(
            username = params.username,
            email = params.email,
            firstName = params.firstName,
            lastName = params.lastName
        )
    }
    
    data class Params(
        val username: String,
        val email: String,
        val firstName: String,
        val lastName: String
    )
}
