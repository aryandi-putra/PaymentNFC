package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.NoParamUseCase
import com.aryandi.paymentnfc.domain.model.Transaction
import com.aryandi.paymentnfc.domain.repository.TransactionRepository

/**
 * Use case to get transactions for card detail screen
 * Organized by date
 */
open class GetCardTransactionsUseCase(
    private val transactionRepository: TransactionRepository
) : NoParamUseCase<Map<String, List<Transaction>>>() {
    
    override suspend fun invoke(): Result<Map<String, List<Transaction>>> {
        return transactionRepository.getTransactions().map { transactions ->
            if (transactions.isEmpty()) {
                // If API returns empty, provide some default mocked groups for design demonstration
                // In a real app, we would handle empty state in UI
                mockGroups()
            } else {
                // Group by date string from API
                transactions.groupBy { it.date }
            }
        }
    }

    private fun mockGroups(): Map<String, List<Transaction>> {
        return mapOf(
            "Today" to listOf(
                Transaction("Transfer from BCA", "Today, 11:00pm", "+$2.95", "confirmed"),
                Transaction("Walmart", "Today, 11:00pm", "-$11.23", "confirmed")
            ),
            "November 28, 2025" to listOf(
                Transaction("Muse Restaurant", "Today, 11:00pm", "-$76.12", "confirmed"),
                Transaction("Zimmermann", "Today, 11:00pm", "-$91.31", "confirmed")
            )
        )
    }
}
