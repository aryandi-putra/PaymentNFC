package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.TransactionRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class GetTransactionsUseCaseTest : BehaviorSpec({
    val repository = mock<TransactionRepository>()
    val useCase = GetTransactionsUseCase(repository)

    Given("a GetTransactionsUseCase") {
        When("invoked") {
            val transactions = listOf(
                Transaction("Test", "Date", "$ 10", "confirmed")
            )
            everySuspend { repository.getTransactions() } returns Result.success(transactions)

            val result = useCase()

            Then("it should return transactions from repository") {
                result.shouldBeSuccess() shouldBe transactions
            }
        }
    }
})
