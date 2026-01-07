package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.dto.LoginResponse
import com.aryandi.paymentnfc.data.network.AuthApiService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class AuthRepositoryImplTest : FunSpec({

    val authApiService = mock<AuthApiService>()
    val repository = AuthRepositoryImpl(authApiService)

    test("login returns mapped User on success") {
        val response = LoginResponse(
            id = 1,
            username = "testuser",
            email = "test@example.com",
            firstName = "John",
            lastName = "Doe",
            gender = "male",
            image = "url",
            accessToken = "access",
            refreshToken = "refresh"
        )
        everySuspend { authApiService.login(any(), any()) } returns Result.success(response)

        val result = repository.login("testuser", "password")

        result.shouldBeSuccess().apply {
            id shouldBe 1
            username shouldBe "testuser"
            fullName shouldBe "John Doe"
        }
    }

    test("login returns failure on api error") {
        val errorMessage = "Login failed"
        everySuspend { authApiService.login(any(), any()) } returns Result.failure(Exception(errorMessage))

        val result = repository.login("testuser", "password")

        result.shouldBeFailure().message shouldBe errorMessage
    }
})
