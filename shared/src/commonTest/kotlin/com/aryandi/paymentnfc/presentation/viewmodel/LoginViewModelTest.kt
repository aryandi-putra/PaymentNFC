package com.aryandi.paymentnfc.presentation.viewmodel

import app.cash.turbine.test
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.usecase.LoginUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val loginUseCase = mock<LoginUseCase>()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onIntent UsernameChanged updates state`() = runTest {
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onIntent(LoginIntent.UsernameChanged("newuser"))
        assertEquals("newuser", viewModel.uiState.value.username)
    }

    @Test
    fun `onIntent PasswordChanged updates state`() = runTest {
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onIntent(LoginIntent.PasswordChanged("newpass"))
        assertEquals("newpass", viewModel.uiState.value.password)
    }

    @Test
    fun `onIntent Submit successful login updates state and emits event`() = runTest {
        // Arrange
        val user = User(1, "u", "e", "f", "l", "g", "i", "a", "r")
        everySuspend { loginUseCase(any()) } returns Result.success(user)
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onIntent(LoginIntent.UsernameChanged("user"))
        viewModel.onIntent(LoginIntent.PasswordChanged("pass"))

        // Act & Assert
        viewModel.events.test {
            viewModel.onIntent(LoginIntent.Submit)
            
            val event = awaitItem()
            assertTrue(event is LoginEvent.LoginSuccess)
            assertEquals(user, event.user)

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertTrue(state.isSuccess)
            assertEquals(user, state.user)
            assertNull(state.error)
        }
    }

    @Test
    fun `onIntent Submit failed login updates state and emits error event`() = runTest {
        // Arrange
        val errorMessage = "Invalid credentials"
        everySuspend { loginUseCase(any()) } returns Result.failure(Exception(errorMessage))
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onIntent(LoginIntent.UsernameChanged("user"))
        viewModel.onIntent(LoginIntent.PasswordChanged("wrong"))

        // Act & Assert
        viewModel.events.test {
            viewModel.onIntent(LoginIntent.Submit)
            
            val event = awaitItem()
            assertTrue(event is LoginEvent.LoginFailure)
            assertEquals(errorMessage, event.message)

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertFalse(state.isSuccess)
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `onIntent ClearError removes error from state`() = runTest {
        // Arrange
        everySuspend { loginUseCase(any()) } returns Result.failure(Exception("Error"))
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onIntent(LoginIntent.Submit)
        testDispatcher.scheduler.runCurrent()
        assertTrue(viewModel.uiState.value.error != null)

        // Act
        viewModel.onIntent(LoginIntent.ClearError)

        // Assert
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `onIntent Reset clears state`() = runTest {
        // Arrange
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.onIntent(LoginIntent.UsernameChanged("user"))
        viewModel.onIntent(LoginIntent.PasswordChanged("pass"))

        // Act
        viewModel.onIntent(LoginIntent.Reset)

        // Assert
        val state = viewModel.uiState.value
        assertEquals("", state.username)
        assertEquals("", state.password)
    }
}
