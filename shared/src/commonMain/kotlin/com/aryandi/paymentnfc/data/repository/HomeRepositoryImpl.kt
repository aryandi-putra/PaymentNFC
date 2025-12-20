package com.aryandi.paymentnfc.data.repository

import com.aryandi.paymentnfc.data.network.HomeApiService
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.HomeRepository

class HomeRepositoryImpl(
    private val homeApiService: HomeApiService
) : HomeRepository {
    
    override suspend fun getTransactions(): Result<List<Transaction>> {
        return homeApiService.getTransfers().mapCatching { dtos ->
            dtos.map { dto ->
                Transaction(
                    title = dto.title,
                    date = dto.date,
                    amount = dto.amount,
                    status = dto.status
                )
            }
        }
    }
}
