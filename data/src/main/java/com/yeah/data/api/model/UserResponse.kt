package com.yeah.data.api.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yeah.data.db.model.UserDbo
import com.yeah.domain.model.ErrorMessage
import com.yeah.domain.model.User
import com.yeah.utils.converters.saveList

data class UserResponse(
    val token: String?,
    val user: DataUser?,
    val errorMessage: DataErrorMessage?,
    //val errorMessage: DataErrorMessage
)

data class DataUser(
    val isAuthenticated: Boolean,
    val id: String,
    val a_hours: Int,
    val email: String,
    val phoneNumber: String,
    val b_hours: Int,
    val c_hours: Int,
    val first_name: String,
    val name: String,
    val value: String,
    val middle_name: String,
    val last_name: String,
    val paid_hours: Int,
    val user_role: List<String>,
)



fun UserResponse.toDbo(): UserDbo? {
    return this.user?.let {
        UserDbo(
        id = it.id,
        email = it.email,
        phoneNumber = it.phoneNumber,
        firstName = it.first_name,
        middleName = it.middle_name,
        lastName = it.last_name,
        isAuthenticated = it.isAuthenticated,
        paidHours = it.paid_hours,
        aHours = it.a_hours,
        bHours = it.b_hours,
        cHours = it.c_hours,
        userRole = saveList(it.user_role)
    )
    }
}

fun UserResponse.toDomain(): Pair<User?, ErrorMessage> {
    var errorList: List<String> = listOf("200")

    return Pair(first = user?.toDomain(), second = ErrorMessage(message = "Login success", error = errorList.toString()));
}

fun DataUser.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        phoneNumber = this.phoneNumber,
        firstName = this.first_name,
        middleName = this.middle_name,
        lastName = this.last_name,
        isAuthenticated = this.isAuthenticated,
        paidHours = this.paid_hours,
        aHours = this.a_hours,
        bHours = this.b_hours,
        cHours = this.c_hours,
        userRole = saveList(this.user_role),
    )
}

data class DataErrorMessage(
    val message: String,
    val error: List<String>
)

fun DataErrorMessage.toDomain(): ErrorMessage{
    return ErrorMessage(
        message = this.message,
        error = this.error.joinToString(separator = " ,"),
    )
}

