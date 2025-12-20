package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.HomeRepository
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe

class GetTransactionsUseCaseTest : StringSpec({
    val repository = mock<HomeRepository>()
    val useCase = GetTransactionsUseCase(repository)

    "invoke calls repository and returns result" {
        val transactions = listOf(
            Transaction("Test", "Date", "$ 10", "confirmed")
        )
        everySuspend { repository.getTransactions() } returns Result.success(transactions)

        val result = useCase()

        result.shouldBeSuccess() shouldBe transactions
    }
})
