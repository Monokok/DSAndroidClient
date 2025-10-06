package com.yeah.dsapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeah.domain.model.Lesson
import com.yeah.domain.model.User
import com.yeah.domain.services.IUserService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LessonsViewModel(private val userService: IUserService) : ViewModel() {
    // Список занятий
    private val _lessons = MutableLiveData<List<Lesson>>()
    val lessons: LiveData<List<Lesson>> get() = _lessons

    suspend fun isUserLoggedIn(): Boolean{
        return userService.isUserLoggedIn();
    }

    suspend fun getLessons(){
        val lessons = userService.getLessons()
        _lessons.value = lessons
    }

    suspend fun getUserRole():  String?{
        val roles =  userService.getUserRoles()
        return if (roles.isEmpty()) null
        else roles.first()
    }

    suspend fun  getLessonsFromDb(){
        val lessonsFromDb = userService.getLessonsFromDb()
        _lessons.value = lessonsFromDb
    }

    // Метод для отмены занятия
    fun tryCancelLesson(id: Int, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val result = userService.tryCancelLesson(id)
                onResult(result)  // Передаем результат обратно в функцию callback
            } catch (e: Exception) {
                onResult("Ошибка: ${e.message}")
            }
        }
    }

    fun fetchLessons(){
        viewModelScope.launch {
            getLessons()
        }
    }
}