package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.HomeRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetTransactionsUseCaseTest {

    private val repository = mock<HomeRepository>()
    private val useCase = GetTransactionsUseCase(repository)

    @Test
    fun `invoke calls repository and returns result`() = runTest {
        // Arrange
        val transactions = listOf(
            Transaction("Test", "Date", "$ 10", "confirmed")
        )
        everySuspend { repository.getTransactions() } returns Result.success(transactions)

        // Act
        val result = useCase()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(transactions, result.getOrNull())
    }
}
