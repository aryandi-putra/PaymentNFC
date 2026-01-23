package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.NoParamUseCase
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.TransactionRepository

open class GetTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) : NoParamUseCase<List<Transaction>>() {
    
    override suspend fun invoke(): Result<List<Transaction>> {
        return transactionRepository.getTransactions()
    }
}
