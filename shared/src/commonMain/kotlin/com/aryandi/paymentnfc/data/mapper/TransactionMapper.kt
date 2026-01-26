package com.aryandi.paymentnfc.data.mapper

import com.aryandi.paymentnfc.data.dto.TransactionDto
import com.aryandi.paymentnfc.domain.model.Transaction

/**
 * Mapper for Transaction data
 */
object TransactionMapper {
    fun toDomain(dto: TransactionDto): Transaction {
        return Transaction(
            title = dto.title,
            date = dto.date,
            amount = dto.amount,
            status = dto.status
        )
    }
}
