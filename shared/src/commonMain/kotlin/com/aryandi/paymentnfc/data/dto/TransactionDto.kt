package com.aryandi.paymentnfc.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val title: String,
    val date: String,
    val amount: String,
    val status: String
)
