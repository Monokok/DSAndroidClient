package com.yeah.data.repository

import com.yeah.data.api.LessonApi
import com.yeah.data.db.LessonDao
import com.yeah.data.db.model.LessonDbo
import com.yeah.data.db.model.toDomain
import com.yeah.domain.model.Lesson
import  com.yeah.domain.repository.IRepository
import com.yeah.utils.ISharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


class PracticeRepository(
    private val lessonApi: LessonApi,
    private val lessonDao: LessonDao,
    private val preferencesManager: ISharedPreferencesManager,
) : IRepository<Lesson> {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private fun getAuthToken(): String? {
        return preferencesManager.getAuthToken()
    }

    override suspend fun getList(): List<Lesson> {
        return lessonDao.getAllLessons().map { it.toDomain() }
    }

    override suspend fun clear() {
        lessonDao.clearLessons()
    }

    override suspend fun getItem(id: Int): Lesson? {
        return lessonDao.getLessonById(id)?.toDomain();//?.toDomain()
    }

    override suspend fun getItem(id: String): Lesson? {
        TODO("Not yet implemented")
    }

    override suspend fun create(item: Lesson) {
        lessonDao.insert(
            LessonDbo(
                item.id,
                item.title,
                item.date,
                item.lessonStatus,
                item.studentName,
                item.teacherName,
                item.description,
                item.category,
                item.teacherPhoneNumber,
                item.studentPhoneNumber,
                item.teacherEmail,
                item.studentEmail
            )
        )
    }

    override suspend fun update(item: Lesson) {
        lessonDao.updateLesson(
            LessonDbo(
                item.id,
                item.title,
                item.date,
                item.lessonStatus,
                item.studentName,
                item.teacherName,
                item.description,
                item.category,
                item.teacherPhoneNumber,
                item.studentPhoneNumber,
                item.teacherEmail,
                item.studentEmail
            )
        )
    }

    override suspend fun delete(id: Int) {
        lessonDao.deleteLessonById(id);//
    }

    override suspend fun delete(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Boolean {
        TODO("Not yet implemented")
    }
}

