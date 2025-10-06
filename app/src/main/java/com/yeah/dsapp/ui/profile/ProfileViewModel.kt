package com.yeah.dsapp.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeah.domain.model.User
import com.yeah.domain.services.IUserService
import com.yeah.utils.ISharedPreferencesManager

class ProfileViewModel(private val userService: IUserService, private val spManager: ISharedPreferencesManager) :
    ViewModel() {
//
//    // LiveData для данных профиля
//    private val _userProfile = MutableLiveData<User>()
//    val userProfile: LiveData<User> get() = _userProfile
//
//    fun loadUserProfile()
//    {
//        try {
//        }
//        catch (e: Exception)
//        {
//
//        }
//    }

    suspend fun logout(): Boolean {
        return userService.logout()
    }

    suspend fun loadProfileData(): User? {
        return userService.getUser();
    }

//    // Метод для загрузки профиля пользователя
//    fun loadUserProfile(userId: String) {
//        viewModelScope.launch {
//            try {
//                val user = userService.getUserProfile(userId)
//                _userProfile.value = user
//            } catch (e: Exception) {
//                // Обработка ошибок (например, вывести сообщение пользователю)
//            }
//        }
//    }

//    // Метод для обновления профиля пользователя
//    fun updateUserProfile(updatedUser: User) {
//        viewModelScope.launch {
//            try {
//                userService.updateUserProfile(updatedUser)
//                _userProfile.value = updatedUser
//            } catch (e: Exception) {
//                // Обработка ошибок
//            }
//        }
//    }

}