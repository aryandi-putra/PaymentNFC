package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.dto.TransactionDto
import com.aryandi.paymentnfc.data.network.HomeApiService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest

class HomeRepositoryImplTest : FunSpec({

    val apiService = mock<HomeApiService>()
    val repository = HomeRepositoryImpl(apiService)

    test("getTransactions returns mapped list on success") {
        runTest {
            // Arrange
            val dtos = listOf(
                TransactionDto("Test", "Date", "$ 10", "confirmed")
            )
            everySuspend { apiService.getTransfers() } returns Result.success(dtos)

            // Act
            val result = repository.getTransactions()

            // Assert
            result.shouldBeSuccess().apply {
                shouldHaveSize(1)
                this[0].title shouldBe "Test"
                this[0].date shouldBe "Date"
                this[0].amount shouldBe "$ 10"
                this[0].status shouldBe "confirmed"
            }
        }
    }

    test("getTransactions returns failure on api error") {
        runTest {
            // Arrange
            val exception = Exception("API Error")
            everySuspend { apiService.getTransfers() } returns Result.failure(exception)

            // Act
            val result = repository.getTransactions()

            // Assert
            result.shouldBeFailure().message shouldBe "API Error"
        }
    }
})
