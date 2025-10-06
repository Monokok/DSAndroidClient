import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeah.domain.model.Lesson
import com.yeah.domain.model.User
import com.yeah.domain.services.IUserService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreatePracticeViewModel(private val userService: IUserService) : ViewModel() {
    private val _teachers = MutableLiveData<List<User>>(emptyList())
    val teachers: LiveData<List<User>> get() = _teachers

    private val _times = MutableLiveData<List<String>>(emptyList())
    val times: LiveData<List<String>> get() = _times


    suspend fun getUserRole(): String {
        val roles: List<String> = userService.getUserRoles();
        return if (roles.contains("student")){
                "student"
        } else if (roles.contains("teacher")){
                "teacher"
        } else "unknown"
    }

    suspend fun isUserLoggedIn(): Boolean{
        return userService.isUserLoggedIn();
    }

    fun loadTeachers() {
        viewModelScope.launch {
            val loadedTeachers = userService.getTeachers() // Получение данных
            _teachers.value = loadedTeachers
        }
    }

    fun loadStudents() {
        viewModelScope.launch {
            val loadedStudents = userService.getStudents() // Получение данных
            _teachers.value = loadedStudents
        }
    }

    fun loadTimes(teacherId: String, selectedDate: String) {
        viewModelScope.launch {
            val loadedTimes = userService.getTimes(teacherId, selectedDate) // Получение данных
            _times.value = loadedTimes
        }
    }

    fun clearTimes() {
        _times.value = emptyList<String>()//очистка
    }

    suspend fun createLesson(teacherId: String, date: String, time: String): Lesson? {
        //val resultLesson = viewModelScope.async {
        val resultLesson = userService.createLesson(teacherId, date, time);
        //}.await()
        return resultLesson
    }
    suspend fun getLessonsFromDb(): List<Lesson>{
        return userService.getLessonsFromDb()
    }

    suspend fun getCurrentUserId(): String {
        return userService.getUser()?.id ?: ""
    }

    suspend fun createLessonByTeacher(studentId: String, date: String, time: String): Lesson? {
        val resultLesson = userService.createLessonByTeacher(studentId, date, time);
        return resultLesson
    }


}