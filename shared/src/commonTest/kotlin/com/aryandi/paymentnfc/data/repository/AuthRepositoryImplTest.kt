package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.dto.LoginResponse
import com.aryandi.paymentnfc.data.network.AuthApiService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class AuthRepositoryImplTest : BehaviorSpec({

    val authApiService = mock<AuthApiService>()
    val repository = AuthRepositoryImpl(authApiService)

    Given("an AuthRepository") {
        When("login is successful") {
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

            Then("it should return mapped User") {
                result.shouldBeSuccess().apply {
                    id shouldBe 1
                    username shouldBe "testuser"
                    fullName shouldBe "John Doe"
                }
            }
        }

        When("login api returns error") {
            val errorMessage = "Login failed"
            everySuspend { authApiService.login(any(), any()) } returns Result.failure(Exception(errorMessage))

            val result = repository.login("testuser", "password")

            Then("it should return failure with error message") {
                result.shouldBeFailure().message shouldBe errorMessage
            }
        }

        When("verifyOtp is called with correct otp 123456") {
            val email = "test@example.com"
            val result = repository.verifyOtp(email, "123456")

            Then("it should return success") {
                result.shouldBeSuccess().apply {
                    username shouldBe "test"
                    this.email shouldBe email
                }
            }
        }

        When("verifyOtp is called with wrong otp") {
            val result = repository.verifyOtp("test@example.com", "wrong")

            Then("it should return failure with 'Invalid OTP'") {
                result.shouldBeFailure().message shouldBe "Invalid OTP"
            }
        }

        When("resendOtp is called") {
            val result = repository.resendOtp("test@example.com")

            Then("it should return success") {
                result.shouldBeSuccess() shouldBe Unit
            }
        }
    }
})
