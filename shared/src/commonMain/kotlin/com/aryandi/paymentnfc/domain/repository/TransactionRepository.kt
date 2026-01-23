package com.aryandi.paymentnfc.domain.repository

import com.aryandi.paymentnfc.domain.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(): Result<List<Transaction>>
}
