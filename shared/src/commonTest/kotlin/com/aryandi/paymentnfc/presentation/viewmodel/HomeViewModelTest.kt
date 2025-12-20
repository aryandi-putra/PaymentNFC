package com.aryandi.paymentnfc.presentation.viewmodel

import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.usecase.GetTransactionsUseCase
import app.cash.turbine.test
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentially
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
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
class HomeViewModelTest {

    private val getTransactionsUseCase = mock<GetTransactionsUseCase>()
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
    fun `initialization loads transactions successfully`() = runTest {
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
            
            assertEquals("test", state.userName)
            assertEquals(transactions, state.transactions)
            assertNull(state.error)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `initialization sets error on failure`() = runTest {
        // Arrange
        val errorMessage = "Network Error"
        everySuspend { getTransactionsUseCase() } returns Result.failure(Exception(errorMessage))

        // Act
        val viewModel = HomeViewModel("test@user.com", getTransactionsUseCase)
        testDispatcher.scheduler.runCurrent()

        // Assert
        val state = viewModel.uiState.value
        assertEquals(errorMessage, state.error)
        assertTrue(state.transactions.isEmpty())
        assertFalse(state.isLoading)
    }

    @Test
    fun `onIntent Refresh reloads data`() = runTest {
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
        assertEquals(transactions1, viewModel.uiState.value.transactions)

        viewModel.onIntent(HomeIntent.Refresh)
        testDispatcher.scheduler.runCurrent()

        // Assert
        assertEquals(transactions2, viewModel.uiState.value.transactions)
    }
}
