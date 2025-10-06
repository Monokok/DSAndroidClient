//package com.yeah.data.repository
//
//import android.util.Log
//import com.google.gson.Gson
//import com.yeah.data.api.UserApi
//import com.yeah.data.api.model.LoginRequest
//import com.yeah.data.api.model.RegisterRequest
//import com.yeah.data.api.model.UserResponse
//import com.yeah.data.api.model.toDomain
//import com.yeah.data.db.LessonDao
//import com.yeah.data.db.UserDao;
//import com.yeah.data.db.model.UserDbo
////import com.yeah.data.db.model.toDomain
////import com.yeah.data.db.model.toUserDbo
//import com.yeah.data.db.model.toDomain
//import com.yeah.data.db.model.toLessonDbo
//import com.yeah.data.db.model.toUserDbo
//import com.yeah.domain.model.ErrorMessage
//import com.yeah.domain.model.Lesson
//import com.yeah.domain.model.LoginResponse
//import com.yeah.domain.model.RegisterResponse
//import com.yeah.domain.model.User
//import  com.yeah.domain.repository.IRepository
//import retrofit2.HttpException
//import com.yeah.utils.SharedPreferencesManager
//import com.yeah.utils.converters.saveList
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import java.util.regex.Pattern
//
////UserRepository взаимодействует с Room через DAO
////получает выборку от API и возвращает её.
//// data/repository/UserRepositoryImpl.kt
//
//
//class LessonRepository(
//    private val userApi: UserApi,
//    private val lessonDao: LessonDao,
//    private val preferencesManager: SharedPreferencesManager,
//) : IRepository<Lesson> {
//
//    private val coroutineScope = CoroutineScope(Dispatchers.Main)
//
//    private fun getAuthToken(): String? {
//        return preferencesManager.getAuthToken()
//    }
//
//    override suspend fun getList(): List<Lesson> {
//        return lessonDao.getAllLessons().map { it.toDomain() }
//    }
//
//    override suspend fun getItem(id: Int): Lesson? {
//        return lessonDao.getLessonById(id)?.toDomain();//?.toDomain()
//    }
//
//    override suspend fun getItem(id: String): Lesson? {
//        TODO("Not yet implemented")
//    }
//
//
//    override suspend fun create(item: Lesson) {
//        lessonDao.insert(item.toLessonDbo());//.toEntity())
//    }
//
//    override suspend fun update(item: Lesson) {
//        lessonDao.updateLesson(item.toLessonDbo());//.toEntity())
//    }
//
//    override suspend fun delete(id: Int) {
//        lessonDao.deleteLessonById(id);//
//    }
//
//    override suspend fun delete(id: String) {
//        TODO("Not yet implemented")
//    }
//
//}
//
