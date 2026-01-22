package com.aryandi.paymentnfc.presentation.viewmodel

import app.cash.turbine.test
import com.aryandi.paymentnfc.domain.model.User
import com.aryandi.paymentnfc.domain.usecase.LoginUseCase
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
class LoginViewModelTest : BehaviorSpec({

    val loginUseCase = mock<LoginUseCase>()
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    Given("a LoginViewModel") {
        When("onIntent UsernameChanged is called") {
            val viewModel = LoginViewModel(loginUseCase)
            viewModel.onIntent(LoginIntent.UsernameChanged("newuser"))

            Then("it should update the username in uiState") {
                viewModel.uiState.value.username shouldBe "newuser"
            }
        }

        When("onIntent PasswordChanged is called") {
            val viewModel = LoginViewModel(loginUseCase)
            viewModel.onIntent(LoginIntent.PasswordChanged("newpass"))

            Then("it should update the password in uiState") {
                viewModel.uiState.value.password shouldBe "newpass"
            }
        }

        When("onIntent Submit is called with successful login") {
            val user = User(1, "u", "e", "f", "l", "g", "i", "a", "r")
            everySuspend { loginUseCase(any()) } returns Result.success(user)
            val viewModel = LoginViewModel(loginUseCase)
            viewModel.onIntent(LoginIntent.UsernameChanged("user"))
            viewModel.onIntent(LoginIntent.PasswordChanged("pass"))

            Then("it should update uiState and emit LoginSuccess event") {
                runTest {
                    viewModel.events.test {
                        viewModel.onIntent(LoginIntent.Submit)
                        
                        val event = awaitItem()
                        (event is LoginEvent.LoginSuccess).shouldBeTrue()
                        (event as LoginEvent.LoginSuccess).user shouldBe user

                        val state = viewModel.uiState.value
                        state.isLoading.shouldBeFalse()
                        state.isSuccess.shouldBeTrue()
                        state.user shouldBe user
                        state.error.shouldBeNull()
                    }
                }
            }
        }

        When("onIntent Submit is called with failed login") {
            val errorMessage = "Invalid credentials"
            everySuspend { loginUseCase(any()) } returns Result.failure(Exception(errorMessage))
            val viewModel = LoginViewModel(loginUseCase)
            viewModel.onIntent(LoginIntent.UsernameChanged("user"))
            viewModel.onIntent(LoginIntent.PasswordChanged("wrong"))

            Then("it should update uiState and emit LoginFailure event") {
                runTest {
                    viewModel.events.test {
                        viewModel.onIntent(LoginIntent.Submit)
                        
                        val event = awaitItem()
                        (event is LoginEvent.LoginFailure).shouldBeTrue()
                        (event as LoginEvent.LoginFailure).message shouldBe errorMessage

                        val state = viewModel.uiState.value
                        state.isLoading.shouldBeFalse()
                        state.isSuccess.shouldBeFalse()
                        state.error shouldBe errorMessage
                    }
                }
            }
        }

        When("onIntent ClearError is called") {
            everySuspend { loginUseCase(any()) } returns Result.failure(Exception("Error"))
            val viewModel = LoginViewModel(loginUseCase)
            runTest {
                viewModel.onIntent(LoginIntent.Submit)
                testDispatcher.scheduler.runCurrent()
            }

            viewModel.onIntent(LoginIntent.ClearError)

            Then("it should remove the error from uiState") {
                viewModel.uiState.value.error.shouldBeNull()
            }
        }

        When("onIntent Reset is called") {
            val viewModel = LoginViewModel(loginUseCase)
            viewModel.onIntent(LoginIntent.UsernameChanged("user"))
            viewModel.onIntent(LoginIntent.PasswordChanged("pass"))

            viewModel.onIntent(LoginIntent.Reset)

            Then("it should clear the uiState") {
                val state = viewModel.uiState.value
                state.username shouldBe ""
                state.password shouldBe ""
            }
        }
    }
})
