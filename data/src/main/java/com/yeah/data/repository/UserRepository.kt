package com.yeah.data.repository

import android.util.Log
import com.google.gson.Gson
import com.yeah.data.api.UserApi
import com.yeah.data.api.model.LoginRequest
import com.yeah.data.api.model.RegisterRequest
import com.yeah.data.api.model.UserResponse
import com.yeah.data.api.model.toDomain
import com.yeah.data.db.UserDao;
import com.yeah.data.db.model.UserDbo
//import com.yeah.data.db.model.toDomain
//import com.yeah.data.db.model.toUserDbo
import com.yeah.data.db.model.toDomain
import com.yeah.data.db.model.toUserDbo
import com.yeah.domain.model.ErrorMessage
import com.yeah.domain.model.LoginResponse
import com.yeah.domain.model.RegisterResponse
import com.yeah.domain.model.User
import  com.yeah.domain.repository.IRepository
import com.yeah.utils.ISharedPreferencesManager
import retrofit2.HttpException
import com.yeah.utils.converters.saveList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.util.regex.Pattern

//UserRepository взаимодействует с Room через DAO
//получает выборку от API и возвращает её.
// data/repository/UserRepositoryImpl.kt


class UserRepository(
    private val userApi: UserApi,
    private val userDao: UserDao,
    private val preferencesManager: ISharedPreferencesManager,
) : IRepository<User> {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private fun getAuthToken(): String? {
        return preferencesManager.getAuthToken()
    }

    override suspend fun login(email: String, password: String): LoginResponse? {
        try {
            val userResponse: UserResponse = userApi.login(LoginRequest(email, password))
            //сохраняем токен в sharedPref
            userResponse.token?.let {
                preferencesManager.saveAuthToken(userResponse.token)//токен возвращается всегда при логине
            }

            userResponse.user?.let {
                userDao.insert(UserDbo(
                    userResponse.user.id,
                    userResponse.user.email,
                    userResponse.user.phoneNumber,
                    userResponse.user.first_name,
                    userResponse.user.middle_name,
                    userResponse.user.last_name,
                    true,
                    userResponse.user.paid_hours,
                    userResponse.user.a_hours,
                    userResponse.user.b_hours,
                    userResponse.user.c_hours,
                    saveList(userResponse.user.user_role),
                ))
                return LoginResponse(user = userResponse.user.toDomain(), errorMessage = userResponse.errorMessage?.toDomain())
            }?: return LoginResponse(user = null, errorMessage = userResponse.errorMessage?.toDomain())
        } catch (e: HttpException) {
            // Обработка ошибки HTTP
            val errorBody = e.response()?.errorBody()?.string()

            errorBody?.let{
                val regex = """"message":"(.*?)".*?"error":\[(.*?)\]""".toRegex()
                val matchResult = regex.find(errorBody)
                if (matchResult != null) {
                    val message = matchResult.groupValues[1] // Извлечение message
                    val errors = matchResult.groupValues[2].replace("\"", "").replace(",", "") // Извлечение ошибок и удаление кавычек и запятых

                    // Объединение строки
                    val result = "$message, $errors"
                    return LoginResponse(null, ErrorMessage(result.toString(), e.message()))
                }

                return LoginResponse(user = null, errorMessage = ErrorMessage(message = errorBody.toString(),
                    error = e.message())
                )
//                try {
//                    // Парсим errorBody в ErrorResponse с помощью Gson
//                    val gson = Gson()
//                    val errorResponse = gson.fromJson(it, ErrorMessage::class.java)
//                    return LoginResponse(null, ErrorMessage(errorResponse.message, errorResponse.error))
//                }
//                catch (ex: Exception){
//                    return LoginResponse(null, ErrorMessage(ex.message.toString(), ex.toString()))
//                }
                //return Pair(null, ErrorMessage(message = errorBody, error = e.message()))
            }
            return LoginResponse(null, ErrorMessage(message = e.message(), error = e.code().toString()))
        } catch (e: Exception) {
            //return Pair(null, ErrorMessage(e.message.toString(), "UserRepository login method error"))
            throw  Exception(e.message)
        }
    }

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        phoneNumber: String,
        email: String,
        password: String,
        passwordConfirm: String,
        role: String,
    ): RegisterResponse? {
        //return super.register(firstName, lastName, middleName, phoneNumber, email, password)
        try {
            val registerResponse: RegisterResponse = userApi.register(RegisterRequest(firstName, lastName, middleName ,phoneNumber,email,password, passwordConfirm, role))
            if (registerResponse.token != null) {
                //preferencesManager.saveAuthToken(registerResponse.token!!)
                return RegisterResponse(registerResponse.token, null)
            }
            else {
                return RegisterResponse(null, registerResponse.errorMessage)
            }
        } catch (e: HttpException){
            // Обработка ошибки HTTP
            val errorBody = e.response()?.errorBody()?.string()

            val regex = """"message":"(.*?)".*?"error":\[(.*?)\]""".toRegex()
            val matchResult = regex.find(errorBody.toString())
            if (matchResult != null) {
                val message = matchResult.groupValues[1] // Извлечение message
                val errors = matchResult.groupValues[2].replace("\"", "").replace(",", "") // Извлечение ошибок и удаление кавычек и запятых

                // Объединение строки
                val result = "$message, $errors"
                return RegisterResponse(null, listOf(errors, message))//errorBody.toString()

            }
            return RegisterResponse(null, listOf(e.message().toString()))//errorBody.toString()

        } catch (e: Exception){
            return RegisterResponse(null, listOf(e.message.toString()))
        }
    }


    override suspend fun getList(): List<User> {
//        return try {
//            val response = userApi.getAllUsers()
//            response.map { UserMapper.toDomain(it) }
//        } catch (e: HttpException) {
//            if (e.code() == 404) {
//                userDao.getAllUsers().map { UserMapper.toDomain(it) }
//            } else {
//                throw e
//            }
//        } catch (e: IOException) {
//            userDao.getAllUsers().map { UserMapper.toDomain(it) }
//        }
        return userDao.getAllUsers().map { it.toDomain() }
    }

    override suspend fun getItem(id: Int): User? {
        return userDao.getUserById(id)?.toDomain();//?.toDomain()
    }

    override suspend fun getItem(id: String): User? {
        return userDao.getUserById(id)?.toDomain();//?.toDomain()
    }

    override suspend fun create(item: User) {
        userDao.insert(item.toUserDbo());//.toEntity())
    }

    override suspend fun update(item: User) {
        userDao.updateUser(item.toUserDbo());//.toEntity())
    }

    override suspend fun delete(id: Int) {
        userDao.deleteUserById(id);//
    }

    override suspend fun delete(id: String) {
        userDao.deleteUserById(id);//
    }

    override suspend fun logout(): Boolean {
        userDao.clearUsers()  // Удаляем все записи о пользователях из таблицы
        preferencesManager.clearAuthToken()
        return userDao.getAllUsers().isEmpty()
    }
}

