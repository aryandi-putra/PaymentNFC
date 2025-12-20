package com.aryandi.paymentnfc.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
)
