package com.yeah.data.api

import com.yeah.data.api.model.LessonRequest
import com.yeah.data.api.model.LessonResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface LessonApi {

    // Получение списка занятий для пользователя по его ID
    @GET("api/PracticeLessons/{id}")
    suspend fun getLessonsByUserId(@Path("id") userId: String): List<LessonResponse>

    // Удаление урока по его ID
    @DELETE("api/PracticeLessons/{id}")
    suspend fun deleteLessonById(@Path("id") lessonId: Int): LessonResponse

    // Получение занятий по ID учителя и дате
    @GET("api/PracticeLessons/{teacherId}/{DayMonthYear}")
    suspend fun getLessonsByTeacherAndDate(
        @Path("teacherId") teacherId: Int,
        @Path("DayMonthYear") date: String
    ): List<LessonResponse>

    // Создание нового урока
    @POST("api/PracticeLessons")
    suspend fun createLesson(@Body lessonRequest: CreateLessonRequest): LessonResponse

    // Обновление урока по его ID и типу
    @PUT("api/PracticeLessons/{lessonId}/{typeId}")
    suspend fun updateLesson(
        @Path("lessonId") lessonId: Int,
        @Path("typeId") typeId: Int,
        @Body lessonRequest: LessonRequest
    ): LessonResponse

    //Получение списка из преподавателей
    @GET("api/Teachers")
    suspend fun getTeacher(): List<UserResponseModel>

    //Получение списка из обучающихся
    @GET("api/Students")
    suspend fun getStudents(): List<UserResponseModel>

    @GET("api/PracticeLessons/{teacherId}/{DayMonthYear}")
    suspend fun fetchAvailableTimes(
        @Path("teacherId") teacherId: String,
        @Path("DayMonthYear") selectedDate: String
    ): List<String>

    @PUT("api/PracticeLessons/{lessonId}")
    suspend fun tryCancelLesson(
        @Path("lessonId") id: Int
    )


    data class UserResponseModel(
        val id: String,
        val email: String,
        val first_name: String,
        val middle_name: String,
        val last_name: String,
        val phoneNumber: String,
    )

    data class CreateLessonRequest(
        val dateTime: String,
        val studentId: String,
        val teacherId: String,
        val category: String,

    )
}