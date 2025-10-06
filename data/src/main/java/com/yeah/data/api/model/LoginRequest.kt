package com.yeah.data.api.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val passwordConfirm: String,
    val role: String,
)


