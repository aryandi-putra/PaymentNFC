package com.aryandi.paymentnfc.presentation.viewmodel

import app.cash.turbine.test
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.usecase.ResendOtpUseCase
import com.aryandi.paymentnfc.domain.usecase.VerifyOtpUseCase
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
class OtpViewModelTest {

    private val verifyOtpUseCase = mock<VerifyOtpUseCase>()
    private val resendOtpUseCase = mock<ResendOtpUseCase>()
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
    fun `onIntent EmailChanged updates state`() = runTest {
        val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
        val email = "test@example.com"
        viewModel.onIntent(OtpIntent.EmailChanged(email))
        assertEquals(email, viewModel.uiState.value.emailOrPhone)
    }

    @Test
    fun `onIntent OtpChanged updates state and clears error`() = runTest {
        val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
        
        // Setup error state
        everySuspend { verifyOtpUseCase(any()) } returns Result.failure(Exception("Error"))
        viewModel.onIntent(OtpIntent.Submit)
        testDispatcher.scheduler.runCurrent()
        assertTrue(viewModel.uiState.value.error != null)

        // Change OTP
        viewModel.onIntent(OtpIntent.OtpChanged("123456"))
        
        assertEquals("123456", viewModel.uiState.value.otp)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `onIntent Submit successful verification updates state and emits event`() = runTest {
        val user = User(1, "u", "e", "f", "l", "g", "i", "a", "r")
        everySuspend { verifyOtpUseCase(any()) } returns Result.success(user)
        val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
        viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))
        viewModel.onIntent(OtpIntent.OtpChanged("123456"))

        viewModel.events.test {
            viewModel.onIntent(OtpIntent.Submit)
            
            val event = awaitItem()
            assertTrue(event is OtpEvent.OtpSuccess)
            assertEquals(user, event.user)

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertTrue(state.isSuccess)
            assertNull(state.error)
        }
    }

    @Test
    fun `onIntent Submit failed verification updates state and emits error event`() = runTest {
        val errorMessage = "Invalid OTP"
        everySuspend { verifyOtpUseCase(any()) } returns Result.failure(Exception(errorMessage))
        val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
        viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))
        viewModel.onIntent(OtpIntent.OtpChanged("111111"))

        viewModel.events.test {
            viewModel.onIntent(OtpIntent.Submit)
            
            val event = awaitItem()
            assertTrue(event is OtpEvent.OtpFailure)
            assertEquals(errorMessage, event.message)

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertFalse(state.isSuccess)
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `onIntent ResendOtp successful updates state and emits event`() = runTest {
        everySuspend { resendOtpUseCase(any()) } returns Result.success(Unit)
        val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
        viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))

        viewModel.events.test {
            viewModel.onIntent(OtpIntent.ResendOtp)
            
            val event = awaitItem()
            assertTrue(event is OtpEvent.ResendSuccess)
            assertEquals("OTP has been resent", event.message)

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertNull(state.error)
        }
    }

    @Test
    fun `onIntent ResendOtp failure updates state and emits error event`() = runTest {
        val errorMessage = "Failed to resend"
        everySuspend { resendOtpUseCase(any()) } returns Result.failure(Exception(errorMessage))
        val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
        viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))

        viewModel.events.test {
            viewModel.onIntent(OtpIntent.ResendOtp)
            
            val event = awaitItem()
            assertTrue(event is OtpEvent.OtpFailure)
            assertEquals(errorMessage, event.message)

            val state = viewModel.uiState.value
            assertFalse(state.isLoading)
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `onIntent ClearError removes error from state`() = runTest {
        everySuspend { verifyOtpUseCase(any()) } returns Result.failure(Exception("Error"))
        val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
        viewModel.onIntent(OtpIntent.Submit)
        testDispatcher.scheduler.runCurrent()
        assertTrue(viewModel.uiState.value.error != null)

        viewModel.onIntent(OtpIntent.ClearError)
        assertNull(viewModel.uiState.value.error)
    }
}
