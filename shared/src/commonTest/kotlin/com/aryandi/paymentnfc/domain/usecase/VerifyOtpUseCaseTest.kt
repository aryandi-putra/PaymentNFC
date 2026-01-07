package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class VerifyOtpUseCaseTest : BehaviorSpec({
    val repository = mock<AuthRepository>()
    val useCase = VerifyOtpUseCase(repository)

    Given("a VerifyOtpUseCase") {
        val email = "test@example.com"
        val validOtp = "123456"
        val invalidOtp = "123"

        When("otp is shorter than 6 digits") {
            val result = useCase(VerifyOtpUseCase.Params(email, invalidOtp))

            Then("it should return failure with validation message") {
                result.shouldBeFailure().message shouldBe "OTP must be 6 digits"
            }
        }

        When("otp is 6 digits and verification is successful") {
            val user = User(
                id = 1,
                username = "test",
                email = email,
                firstName = "F",
                lastName = "L",
                gender = "G",
                image = "I",
                accessToken = "A",
                refreshToken = "R"
            )
            everySuspend { repository.verifyOtp(email, validOtp) } returns Result.success(user)

            val result = useCase(VerifyOtpUseCase.Params(email, validOtp))

            Then("it should return success with user") {
                result.shouldBeSuccess() shouldBe user
            }
        }

        When("otp is 6 digits and verification fails") {
            val errorMessage = "Invalid OTP"
            everySuspend { repository.verifyOtp(email, validOtp) } returns Result.failure(Exception(errorMessage))

            val result = useCase(VerifyOtpUseCase.Params(email, validOtp))

            Then("it should return failure with error message") {
                result.shouldBeFailure().message shouldBe errorMessage
            }
        }
    }
})
