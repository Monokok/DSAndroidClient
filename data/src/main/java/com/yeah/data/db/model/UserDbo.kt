package com.yeah.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yeah.domain.model.User


//dbo - Database object
@Entity(tableName = "users")
data class UserDbo(
    @PrimaryKey val id: String,
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

fun UserDbo.toDomain(): User { //перевод в Domain
    return User(
        id = this.id,
        email = this.email,
        phoneNumber = this.phoneNumber,
        firstName = this.firstName,
        middleName = this.middleName,
        lastName = this.lastName,
        isAuthenticated = this.isAuthenticated,
        paidHours = this.paidHours,
        aHours = this.aHours,
        bHours = this.bHours,
        cHours = this.cHours,
        userRole = this.userRole,
    )
}


fun User.toUserDbo(): UserDbo { //из domainObj в dboObject
    return UserDbo(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        email = this.email,
        phoneNumber = this.phoneNumber,
        aHours = this.aHours,
        bHours = this.bHours,
        cHours = this.cHours,
        isAuthenticated = this.isAuthenticated,
        paidHours = this.paidHours,
        userRole = this.userRole
    )
}

