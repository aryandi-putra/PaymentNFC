package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.dto.LoginResponse
import com.aryandi.paymentnfc.data.network.AuthApiService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRepositoryImplTest {

    private val authApiService = mock<AuthApiService>()
    private val repository = AuthRepositoryImpl(authApiService)

    @Test
    fun `login returns mapped User on success`() = runTest {
        // Arrange
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

        // Act
        val result = repository.login("testuser", "password")

        // Assert
        assertTrue(result.isSuccess)
        val user = result.getOrNull()
        assertEquals(1, user?.id)
        assertEquals("testuser", user?.username)
        assertEquals("John Doe", user?.fullName)
    }

    @Test
    fun `login returns failure on api error`() = runTest {
        // Arrange
        val exception = Exception("Login failed")
        everySuspend { authApiService.login(any(), any()) } returns Result.failure(exception)

        // Act
        val result = repository.login("testuser", "password")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Login failed", result.exceptionOrNull()?.message)
    }
}
