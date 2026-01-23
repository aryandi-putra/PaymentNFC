package com.aryandi.paymentnfc.domain.usecase

import com.aryandi.paymentnfc.domain.base.NoParamUseCase
import com.aryandi.paymentnfc.domain.model.Transaction

/**
 * Use case to get transactions for card detail screen
 * Returns mock data matching the design
 */
open class GetCardTransactionsUseCase : NoParamUseCase<Map<String, List<Transaction>>>() {
    
    override suspend fun invoke(): Result<Map<String, List<Transaction>>> {
        val todayTransactions = listOf(
            Transaction("Transfer from BCA", "Today, 11:00pm", "+$2.95", "confirmed"),
            Transaction("Walmart", "Today, 11:00pm", "-$11.23", "confirmed"),
            Transaction("Muse Restaurant", "Today, 11:00pm", "-$76.12", "confirmed"),
            Transaction("Zimmermann", "Today, 11:00pm", "-$91.31", "confirmed"),
            Transaction("Transfer from BCA", "Today, 11:00pm", "+$2.95", "confirmed"),
            Transaction("Walmart", "Today, 11:00pm", "-$11.23", "confirmed")
        )
        
        val nov28Transactions = listOf(
            Transaction("Transfer from BCA", "Today, 11:00pm", "+$2.95", "confirmed"),
            Transaction("Walmart", "Today, 11:00pm", "-$11.23", "confirmed"),
            Transaction("Muse Restaurant", "Today, 11:00pm", "-$76.12", "confirmed"),
            Transaction("Zimmermann", "Today, 11:00pm", "-$91.31", "confirmed"),
            Transaction("Transfer from BCA", "Today, 11:00pm", "+$2.95", "confirmed"),
            Transaction("Walmart", "Today, 11:00pm", "-$11.23", "confirmed")
        )
        
        val nov27Transactions = listOf(
            Transaction("Transfer from BCA", "Today, 11:00pm", "+$2.95", "confirmed"),
            Transaction("Walmart", "Today, 11:00pm", "-$11.23", "confirmed"),
            Transaction("Muse Restaurant", "Today, 11:00pm", "-$76.12", "confirmed"),
            Transaction("Zimmermann", "Today, 11:00pm", "-$91.31", "confirmed"),
            Transaction("Transfer from BCA", "Today, 11:00pm", "+$2.95", "confirmed"),
            Transaction("Walmart", "Today, 11:00pm", "-$11.23", "confirmed")
        )
        
        return Result.success(
            mapOf(
                "Today" to todayTransactions,
                "November 28, 2025" to nov28Transactions,
                "November 27, 2025" to nov27Transactions
            )
        )
    }
}
