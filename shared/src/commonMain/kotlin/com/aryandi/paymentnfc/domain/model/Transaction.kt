package com.aryandi.paymentnfc.domain.model

data class Transaction(
    val title: String,
    val date: String,
    val amount: String,
    val status: String
)
