package com.yeah.domain.services

import com.yeah.domain.model.Lesson
import com.yeah.domain.model.LoginResponse
import com.yeah.domain.model.RegisterResponse
import com.yeah.domain.model.User

public interface IUserService {
    suspend fun login(email: String, password: String): LoginResponse?
    suspend fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        phoneNumber: String,
        email: String,
        password: String,
        passwordConfirm: String,
        role: String,
    ): RegisterResponse?
    suspend fun logout(): Boolean
    suspend fun getUser(): User?
    suspend fun isUserLoggedIn (): Boolean
    suspend fun getLessons(): List<Lesson>
    suspend fun getLessonsFromDb(): List<Lesson>
    suspend fun getUserRoles(): List<String>
    suspend fun getTeachers(): List<User>
    suspend fun getTimes(teacherId: String, selectedDate: String): List<String> //получение списка из доступного времени для записи на занятие
    suspend fun createLesson(teacherId: String, date: String, time: String): Lesson?
    suspend fun createLessonByTeacher(studentId: String, date: String, time: String): Lesson?

    suspend fun getStudents(): List<User>
    suspend fun tryCancelLesson(id: Int): String

}