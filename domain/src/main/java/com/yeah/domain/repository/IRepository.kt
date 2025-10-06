package com.yeah.domain.repository

import com.yeah.domain.model.ErrorMessage
import com.yeah.domain.model.LoginResponse
import com.yeah.domain.model.RegisterResponse
import com.yeah.domain.model.User



public interface IRepository<T> where T : Any { // Обобщенный интерфейс репозитория для сущностей приложения
    suspend fun getList(): List<T> // Получение всех объектов
    suspend fun getItem(id: Int): T? // Получение объекта по ID
    suspend fun getItem(id: String): T? // Получение объекта по ID
    suspend fun create(item: T) // Создание объекта
    suspend fun update(item: T) // Обновление объекта
    suspend fun delete(id: Int) // Удаление объекта по ID
    suspend fun delete(id: String) // Удаление объекта по ID
    suspend fun login(email: String, password: String): LoginResponse? = null //в зависимости от реализации - возврат null
    suspend fun logout(): Boolean? = null //в зависимости от реализации - возврат null

    suspend fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        phoneNumber: String,
        email: String,
        password: String,
        passwordConfirm: String,
        role: String
    ): RegisterResponse? = null

    suspend fun clear() //удаление всех объектов
}
