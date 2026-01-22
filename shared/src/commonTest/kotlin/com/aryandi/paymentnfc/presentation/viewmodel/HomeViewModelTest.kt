package com.aryandi.paymentnfc.presentation.viewmodel

import app.cash.turbine.test
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.usecase.GetTransactionsUseCase
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentially
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BehaviorSpec({

    val getTransactionsUseCase = mock<GetTransactionsUseCase>()
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    Given("a HomeViewModel") {
        When("initialized with successful transaction load") {
            val transactions = listOf(
                Transaction("Test", "Date", "$ 10", "confirmed")
            )
            everySuspend { getTransactionsUseCase() } returns Result.success(transactions)

            val viewModel = HomeViewModel("test@user.com", getTransactionsUseCase)

            Then("it should set the username and transactions in uiState") {
                viewModel.uiState.test {
                    var state = awaitItem()
                    while (state.userName == "Guest" && state.error == null) {
                        state = awaitItem()
                    }
                    
                    state.userName shouldBe "test"
                    state.transactions shouldBe transactions
                    state.error.shouldBeNull()
                    state.isLoading.shouldBeFalse()
                }
            }
        }

        When("initialized with failed transaction load") {
            val errorMessage = "Network Error"
            everySuspend { getTransactionsUseCase() } returns Result.failure(Exception(errorMessage))

            val viewModel = HomeViewModel("test@user.com", getTransactionsUseCase)
            testDispatcher.scheduler.runCurrent()

            Then("it should set the error message in uiState") {
                val state = viewModel.uiState.value
                state.error shouldBe errorMessage
                state.transactions.shouldBeEmpty()
                state.isLoading.shouldBeFalse()
            }
        }

        When("onIntent Refresh is called") {
            val transactions1 = listOf(Transaction("T1", "D1", "$ 1", "confirmed"))
            val transactions2 = listOf(Transaction("T2", "D2", "$ 2", "confirmed"))
            everySuspend { getTransactionsUseCase() } sequentially {
                returns(Result.success(transactions1))
                returns(Result.success(transactions2))
            }

            val viewModel = HomeViewModel("user", getTransactionsUseCase)
            testDispatcher.scheduler.runCurrent()

            viewModel.onIntent(HomeIntent.Refresh)
            testDispatcher.scheduler.runCurrent()

            Then("it should reload and update transactions in uiState") {
                viewModel.uiState.value.transactions shouldBe transactions2
            }
        }
    }
})
