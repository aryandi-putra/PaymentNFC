package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.repository.AuthRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class ResendOtpUseCaseTest : BehaviorSpec({
    val repository = mock<AuthRepository>()
    val useCase = ResendOtpUseCase(repository)

    Given("a ResendOtpUseCase") {
        val email = "test@example.com"

        When("resend otp is successful") {
            everySuspend { repository.resendOtp(email) } returns Result.success(Unit)

            val result = useCase(email)

            Then("it should return success") {
                result.shouldBeSuccess() shouldBe Unit
            }
        }

        When("resend otp fails") {
            val errorMessage = "Failed to resend"
            everySuspend { repository.resendOtp(email) } returns Result.failure(Exception(errorMessage))

            val result = useCase(email)

            Then("it should return failure") {
                result.shouldBeFailure().message shouldBe errorMessage
            }
        }
    }
})
