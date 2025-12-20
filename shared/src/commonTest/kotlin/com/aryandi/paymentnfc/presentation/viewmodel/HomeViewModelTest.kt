package com.aryandi.paymentnfc.presentation.viewmodel

import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.usecase.GetTransactionsUseCase
import app.cash.turbine.test
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentially
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : StringSpec({

    val getTransactionsUseCase = mock<GetTransactionsUseCase>()
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    "initialization loads transactions successfully" {
        // Arrange
        val transactions = listOf(
            Transaction("Test", "Date", "$ 10", "confirmed")
        )
        everySuspend { getTransactionsUseCase() } returns Result.success(transactions)

        // Act & Assert
        val viewModel = HomeViewModel("test@user.com", getTransactionsUseCase)
        
        viewModel.uiState.test {
            // Wait for the state where userName is correctly set (after loading)
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

    "initialization sets error on failure" {
        // Arrange
        val errorMessage = "Network Error"
        everySuspend { getTransactionsUseCase() } returns Result.failure(Exception(errorMessage))

        // Act
        val viewModel = HomeViewModel("test@user.com", getTransactionsUseCase)
        testDispatcher.scheduler.runCurrent()

        // Assert
        val state = viewModel.uiState.value
        state.error shouldBe errorMessage
        state.transactions.shouldBeEmpty()
        state.isLoading.shouldBeFalse()
    }

    "onIntent Refresh reloads data" {
        // Arrange
        val transactions1 = listOf(Transaction("T1", "D1", "$ 1", "confirmed"))
        val transactions2 = listOf(Transaction("T2", "D2", "$ 2", "confirmed"))
        everySuspend { getTransactionsUseCase() } sequentially {
            returns(Result.success(transactions1))
            returns(Result.success(transactions2))
        }

        // Act
        val viewModel = HomeViewModel("user", getTransactionsUseCase)
        testDispatcher.scheduler.runCurrent()
        viewModel.uiState.value.transactions shouldBe transactions1

        viewModel.onIntent(HomeIntent.Refresh)
        testDispatcher.scheduler.runCurrent()

        // Assert
        viewModel.uiState.value.transactions shouldBe transactions2
    }
})
