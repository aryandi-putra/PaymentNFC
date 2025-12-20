package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.repository.AuthRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginUseCaseTest {

    private val repository = mock<AuthRepository>()
    private val useCase = LoginUseCase(repository)

    @Test
    fun `invoke calls repository and returns user on success`() = runTest {
        // Arrange
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

        // Act
        val result = useCase(LoginUseCase.Params("user", "pass"))

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(user, result.getOrNull())
    }

    @Test
    fun `invoke returns failure on repository error`() = runTest {
        // Arrange
        everySuspend { repository.login(any(), any()) } returns Result.failure(Exception("Error"))

        // Act
        val result = useCase(LoginUseCase.Params("user", "pass"))

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Error", result.exceptionOrNull()?.message)
    }
}
