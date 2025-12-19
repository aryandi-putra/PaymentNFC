package com.aryandi.paymentnfc.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String,
    val accessToken: String,
    val refreshToken: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}
