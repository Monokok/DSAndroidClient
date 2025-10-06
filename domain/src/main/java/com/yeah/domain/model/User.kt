package com.yeah.domain.model

data class LoginResponse(
    val user: User?,
    val errorMessage: ErrorMessage?
)

data class RegisterResponse(
    val token: String?, //при неудаче токен не вернется
    val errorMessage: List<String>? //при Ok() ошибок не будет
)

data class User(
    val id: String,
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val isAuthenticated: Boolean,
    val paidHours: Int,
    val aHours: Int,
    val bHours: Int,
    val cHours: Int,
    val userRole: String,
)

data class ErrorMessage(
    val message: String,
    val error: String
)
//
//data class LoginResult(
//    val user: User?,
//    val errorMessage: ErrorMessage?,
//)



