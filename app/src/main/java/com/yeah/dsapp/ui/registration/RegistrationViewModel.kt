package com.yeah.dsapp.ui.registration

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeah.domain.services.IUserService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.coroutineContext

class RegistrationViewModel(private var service: IUserService) :
    ViewModel()  {

        private val _errorEvent = MutableLiveData<String>()
        val errorEvent: LiveData<String> get() = _errorEvent

        private val _successEvent = MutableLiveData<String>()
        val successEvent: LiveData<String> get() = _successEvent

        suspend fun register(
            firstName: String,
            lastName: String,
            middleName: String,
            phoneNumber: String,
            email: String,
            password: String,
            confirmPassword: String,
            role: String
        ) {
            try{
                var registerResultDeffered = coroutineScope {
                    async { service.register(firstName,
                        lastName, middleName,
                        phoneNumber, email,
                        password, confirmPassword,
                        role);
                    }
                }

                var registerResult = registerResultDeffered.await()

                if (registerResult == null){
                    _errorEvent.value = "Неизвестная ошибка"
                }
                else {
                    if (registerResult.token != null){
                        _successEvent.value = "Регистрация успешна"
                    }
                    else {
                        if (registerResult.errorMessage != null) {
                            _errorEvent.value =
                                registerResult.errorMessage!!.joinToString(separator = ", ")
                        }
                        else {
                            _errorEvent.value = "Неизвестная ошибка. Ответ сервера пуст"
                        }
                    }
                }
            }
            catch (e: Exception) {
                _errorEvent.value = e.message
            }
        }
}