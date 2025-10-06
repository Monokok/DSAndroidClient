package com.yeah.data.repository

import com.yeah.data.api.LessonApi
import com.yeah.data.api.UserApi
import com.yeah.data.db.LessonDao
import com.yeah.data.db.UserDao
import com.yeah.domain.model.Lesson
import com.yeah.domain.model.User
import com.yeah.domain.repository.IDatabaseRepository
import com.yeah.domain.repository.IRepository
import com.yeah.utils.ISharedPreferencesManager

class DbRepository(private val api: UserApi,
                   private val lessonApi: LessonApi,
                   private val dao: UserDao,
                   private val lessonDao: LessonDao,
                   private val preferencesManager: ISharedPreferencesManager,
) : IDatabaseRepository {//первичный конструктор

    //private val db: AppDatabase = AppDatabase.getDatabase(context)
    //private  val api: UserApi = ApiService.createUserApi();
    //private  val dao: UserDao = db.userDao()
    //private val preferencesManager: SharedPreferencesManager = SharedPreferencesManager(context)


    // Ленивая инициализация репозитория
    private val userRepository: IRepository<User> by lazy {
        UserRepository(api, dao, preferencesManager)
    }

    private val lessonsRepository: IRepository<Lesson> by lazy {
        PracticeRepository(lessonApi, lessonDao, preferencesManager)
    }

    // Геттер для свойства users
    override var users: IRepository<User>
        get() = userRepository
        set(value){}

    // Геттер для свойства lessons
    override var lessons: IRepository<Lesson>
        get() = lessonsRepository
        set(value){}
}