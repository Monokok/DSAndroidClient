package com.yeah.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import com.yeah.data.api.model.LoginRequest
import com.yeah.data.api.model.RegisterRequest
import com.yeah.data.api.model.UserResponse
import com.yeah.domain.model.RegisterResponse

interface UserApi {

    @POST("api/account/login")
    suspend fun login(@Body request: LoginRequest): UserResponse

    @POST("api/account/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
//    @GET("users")
//    suspend fun getUsers(@Body request: List<User>): UserResponse
}