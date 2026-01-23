package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.mapper.TransactionMapper
import com.aryandi.paymentnfc.data.network.TransactionApiService
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.TransactionRepository

class TransactionRepositoryImpl(
    private val transactionApiService: TransactionApiService
) : TransactionRepository {
    
    override suspend fun getTransactions(): Result<List<Transaction>> {
        return transactionApiService.getTransfers().mapCatching { dtos ->
            dtos.map { TransactionMapper.toDomain(it) }
        }
    }
}
