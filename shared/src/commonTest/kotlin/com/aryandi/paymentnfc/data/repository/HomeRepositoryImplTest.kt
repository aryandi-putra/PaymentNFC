package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.dto.TransactionDto
import com.aryandi.paymentnfc.data.network.HomeApiService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeRepositoryImplTest {

    private val apiService = mock<HomeApiService>()
    private val repository = HomeRepositoryImpl(apiService)

    @Test
    fun `getTransactions returns mapped list on success`() = runTest {
        // Arrange
        val dtos = listOf(
            TransactionDto("Test", "Date", "$ 10", "confirmed")
        )
        everySuspend { apiService.getTransfers() } returns Result.success(dtos)

        // Act
        val result = repository.getTransactions()

        // Assert
        assertTrue(result.isSuccess)
        val transactions = result.getOrNull()
        assertEquals(1, transactions?.size)
        assertEquals("Test", transactions?.get(0)?.title)
        assertEquals("Date", transactions?.get(0)?.date)
        assertEquals("$ 10", transactions?.get(0)?.amount)
        assertEquals("confirmed", transactions?.get(0)?.status)
    }

    @Test
    fun `getTransactions returns failure on api error`() = runTest {
        // Arrange
        val exception = Exception("API Error")
        everySuspend { apiService.getTransfers() } returns Result.failure(exception)

        // Act
        val result = repository.getTransactions()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("API Error", result.exceptionOrNull()?.message)
    }
}
