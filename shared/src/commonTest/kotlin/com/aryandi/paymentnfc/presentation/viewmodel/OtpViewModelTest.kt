package com.aryandi.paymentnfc.presentation.viewmodel

import app.cash.turbine.test
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.usecase.ResendOtpUseCase
import com.aryandi.paymentnfc.domain.usecase.VerifyOtpUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class OtpViewModelTest : BehaviorSpec({

    val verifyOtpUseCase = mock<VerifyOtpUseCase>()
    val resendOtpUseCase = mock<ResendOtpUseCase>()
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    Given("an OtpViewModel") {
        When("onIntent EmailChanged is called") {
            val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
            val email = "test@example.com"
            viewModel.onIntent(OtpIntent.EmailChanged(email))

            Then("it should update the emailOrPhone in uiState") {
                viewModel.uiState.value.emailOrPhone shouldBe email
            }
        }

        When("onIntent OtpChanged is called") {
            val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
            
            // Setup error state
            everySuspend { verifyOtpUseCase(any()) } returns Result.failure(Exception("Error"))
            runTest {
                viewModel.onIntent(OtpIntent.Submit)
                testDispatcher.scheduler.runCurrent()
            }
            (viewModel.uiState.value.error != null).shouldBeTrue()

            // Change OTP
            viewModel.onIntent(OtpIntent.OtpChanged("123456"))

            Then("it should update otp and clear the error in uiState") {
                viewModel.uiState.value.otp shouldBe "123456"
                viewModel.uiState.value.error.shouldBeNull()
            }
        }

        When("onIntent Submit is called with successful verification") {
            val user = User(1, "u", "e", "f", "l", "g", "i", "a", "r")
            everySuspend { verifyOtpUseCase(any()) } returns Result.success(user)
            val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
            viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))
            viewModel.onIntent(OtpIntent.OtpChanged("123456"))

            Then("it should update uiState and emit OtpSuccess event") {
                runTest {
                    viewModel.events.test {
                        viewModel.onIntent(OtpIntent.Submit)
                        
                        val event = awaitItem()
                        (event is OtpEvent.OtpSuccess).shouldBeTrue()
                        (event as OtpEvent.OtpSuccess).user shouldBe user

                        val state = viewModel.uiState.value
                        state.isLoading.shouldBeFalse()
                        state.isSuccess.shouldBeTrue()
                        state.error.shouldBeNull()
                    }
                }
            }
        }

        When("onIntent Submit is called with failed verification") {
            val errorMessage = "Invalid OTP"
            everySuspend { verifyOtpUseCase(any()) } returns Result.failure(Exception(errorMessage))
            val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
            viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))
            viewModel.onIntent(OtpIntent.OtpChanged("111111"))

            Then("it should update uiState and emit OtpFailure event") {
                runTest {
                    viewModel.events.test {
                        viewModel.onIntent(OtpIntent.Submit)
                        
                        val event = awaitItem()
                        (event is OtpEvent.OtpFailure).shouldBeTrue()
                        (event as OtpEvent.OtpFailure).message shouldBe errorMessage

                        val state = viewModel.uiState.value
                        state.isLoading.shouldBeFalse()
                        state.isSuccess.shouldBeFalse()
                        state.error shouldBe errorMessage
                    }
                }
            }
        }

        When("onIntent ResendOtp is called successfully") {
            everySuspend { resendOtpUseCase(any()) } returns Result.success(Unit)
            val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
            viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))

            Then("it should update uiState and emit ResendSuccess event") {
                runTest {
                    viewModel.events.test {
                        viewModel.onIntent(OtpIntent.ResendOtp)
                        
                        val event = awaitItem()
                        (event is OtpEvent.ResendSuccess).shouldBeTrue()
                        (event as OtpEvent.ResendSuccess).message shouldBe "OTP has been resent"

                        val state = viewModel.uiState.value
                        state.isLoading.shouldBeFalse()
                        state.error.shouldBeNull()
                    }
                }
            }
        }

        When("onIntent ResendOtp fails") {
            val errorMessage = "Failed to resend"
            everySuspend { resendOtpUseCase(any()) } returns Result.failure(Exception(errorMessage))
            val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
            viewModel.onIntent(OtpIntent.EmailChanged("test@example.com"))

            Then("it should update uiState and emit OtpFailure event") {
                runTest {
                    viewModel.events.test {
                        viewModel.onIntent(OtpIntent.ResendOtp)
                        
                        val event = awaitItem()
                        (event is OtpEvent.OtpFailure).shouldBeTrue()
                        (event as OtpEvent.OtpFailure).message shouldBe errorMessage

                        val state = viewModel.uiState.value
                        state.isLoading.shouldBeFalse()
                        state.error shouldBe errorMessage
                    }
                }
            }
        }

        When("onIntent ClearError is called") {
            everySuspend { verifyOtpUseCase(any()) } returns Result.failure(Exception("Error"))
            val viewModel = OtpViewModel(verifyOtpUseCase, resendOtpUseCase)
            runTest {
                viewModel.onIntent(OtpIntent.Submit)
                testDispatcher.scheduler.runCurrent()
            }
            (viewModel.uiState.value.error != null).shouldBeTrue()

            viewModel.onIntent(OtpIntent.ClearError)

            Then("it should remove the error from uiState") {
                viewModel.uiState.value.error.shouldBeNull()
            }
        }
    }
})
