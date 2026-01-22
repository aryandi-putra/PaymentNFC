package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.dto.TransactionDto
import com.aryandi.paymentnfc.data.network.HomeApiService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest

class HomeRepositoryImplTest : BehaviorSpec({

    val apiService = mock<HomeApiService>()
    val repository = HomeRepositoryImpl(apiService)

    Given("a HomeRepository") {
        When("getTransactions is successful") {
            runTest {
                val dtos = listOf(
                    TransactionDto("Test", "Date", "$ 10", "confirmed")
                )
                everySuspend { apiService.getTransfers() } returns Result.success(dtos)

                val result = repository.getTransactions()

                Then("it should return mapped list of transactions") {
                    result.shouldBeSuccess().apply {
                        shouldHaveSize(1)
                        this[0].title shouldBe "Test"
                        this[0].date shouldBe "Date"
                        this[0].amount shouldBe "$ 10"
                        this[0].status shouldBe "confirmed"
                    }
                }
            }
        }

        When("getTransactions api returns error") {
            runTest {
                val exception = Exception("API Error")
                everySuspend { apiService.getTransfers() } returns Result.failure(exception)

                val result = repository.getTransactions()

                Then("it should return failure with error message") {
                    result.shouldBeFailure().message shouldBe "API Error"
                }
            }
        }
    }
})
