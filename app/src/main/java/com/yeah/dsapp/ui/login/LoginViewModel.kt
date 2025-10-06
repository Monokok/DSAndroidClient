package com.yeah.dsapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeah.domain.model.ErrorMessage
import com.yeah.domain.model.LoginResponse
import com.yeah.domain.model.User
import com.yeah.domain.services.IUserService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val userService: IUserService): ViewModel() {

    suspend fun loginIn(login: String?, password: String?): Pair<String, String> {
        return try {
            try {
                //запуск userService.login в асинхронной корутине
                val loginResultDeffered = coroutineScope {
                    async { userService.login(login.toString(), password.toString())}
                }

                val loginResult: LoginResponse? = loginResultDeffered.await()//дожидаемся данных

                if (loginResult == null) return Pair("Ошибка входа", "Неизвестная ошибка")
                else{
                    if (loginResult.user != null){
                        return Pair(loginResult.user!!.email, "Login success")
                    }
                    return Pair(loginResult.errorMessage?.message ?: "Неизвестная ошибка",
                        loginResult.errorMessage?.message ?: ("Неизвестная ошибка" +
                                loginResult.errorMessage?.error)
                    )
                }
//
//                loginResult?.user?.let { return Pair(
//                    loginResult.user!!.email,
//                    "Вход успешен"
//                )}
//                loginResult?.errorMessage?.let { return Pair(it?.message, it?.error) }

            } catch (e: Exception) {
                Pair("", "Ошибка при попытке входа: ${e.message}")
            }
        } catch (e: Exception){
            throw Exception(e.message)
        }
    }


    private fun checkPasswordAndLogin(login: String?, password: String?): Boolean {
        // Проверяем, что логин не пустой и не null
        if (login.isNullOrEmpty()) {
            return false
        }

        // Проверяем, что пароль не пустой и не null
        if (password.isNullOrEmpty()) {
            return false
        }

        // Проверяем минимальную длину пароля
        if (password.length < 8) {
            return false
        }

//        // Проверяем, что пароль содержит хотя бы одну цифру
//        if (!password.any { it.isDigit() }) {
//            return false
//        }

        // Проверяем, что пароль содержит хотя бы одну букву
        if (!password.any { it.isLetter() }) {
            return false
        }

//        // Проверяем, что пароль содержит хотя бы один специальный символ
//        if (!password.any { !it.isLetterOrDigit() }) {
//            return false
//        }

        return true
    }

}