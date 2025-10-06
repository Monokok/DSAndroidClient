package com.yeah.data.api

import com.yeah.utils.ISharedPreferencesManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor

object ApiService  {

    private const val BASE_URL = "http://192.168.31.18:5050/";//"http://10.0.2.2:5050/"  192.168.31.18 192.168.31.18:5050

    // Логирование запросов (для отладки)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Создаем Interceptor для добавления токена в запросы
    private fun createTokenInterceptor(spManager: ISharedPreferencesManager) = okhttp3.Interceptor { chain ->
        val token = spManager.getAuthToken()

        // Если токен есть, добавляем его в заголовок Authorization
        val newRequest: Request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")  // Добавляем токен в заголовок
                .build()
        } else {
            chain.request()  // Если токен отсутствует, просто выполняем запрос без добавления заголовка
        }

        // Выполняем запрос с новым заголовком
        chain.proceed(newRequest)
    }

    // OkHttpClient с логированием
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Метод для создания экземпляра Retrofit и реализации интерфейса
    fun createUserApi(spManager: ISharedPreferencesManager): UserApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Логирование запросов
            .addInterceptor(createTokenInterceptor(spManager)) // Добавляем Interceptor для токена
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Используем настроенный OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // конвертер для JSON
            .build()
        return retrofit.create(UserApi::class.java) // Генерация реализации интерфейса

//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create()) // конвертер для JSON
//            .build()
//
//        return retrofit.create(UserApi::class.java) // Генерация реализации интерфейса
    }

    fun createLessonApi(spManager: ISharedPreferencesManager): LessonApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)  // Логирование запросов
            .addInterceptor(createTokenInterceptor(spManager)) // Добавляем Interceptor для токена
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // Используем настроенный OkHttpClient
            .addConverterFactory(GsonConverterFactory.create()) // конвертер для JSON
            .build()

        return retrofit.create(LessonApi::class.java) // Генерация реализации интерфейса
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create()) // конвертер для JSON
//            .build()
//
//        return retrofit.create(LessonApi::class.java) // Генерация реализации интерфейса
    }
}
