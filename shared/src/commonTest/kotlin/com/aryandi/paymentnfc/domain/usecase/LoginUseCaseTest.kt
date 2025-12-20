package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class LoginUseCaseTest : BehaviorSpec({
    val repository = mock<AuthRepository>()
    val useCase = LoginUseCase(repository)

    Given("a LoginUseCase") {
        When("login is successful") {
            val user = User(
                id = 1,
                username = "user",
                email = "email",
                firstName = "F",
                lastName = "L",
                gender = "G",
                image = "I",
                accessToken = "A",
                refreshToken = "R"
            )
            everySuspend { repository.login("user", "pass") } returns Result.success(user)

            val result = useCase(LoginUseCase.Params("user", "pass"))

            Then("it should return the user") {
                result.shouldBeSuccess() shouldBe user
            }
        }

        When("repository returns failure") {
            val errorMessage = "Error"
            everySuspend { repository.login(any(), any()) } returns Result.failure(Exception(errorMessage))

            val result = useCase(LoginUseCase.Params("user", "pass"))

            Then("it should return failure with the error message") {
                result.shouldBeFailure().message shouldBe errorMessage
            }
        }
    }
})
