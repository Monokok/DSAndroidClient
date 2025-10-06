package com.yeah.data

import FakeSharedPreferencesManager
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yeah.data.api.LessonApi
import com.yeah.data.db.AppDatabase
import com.yeah.data.db.LessonDao
import com.yeah.data.db.model.LessonDbo
import com.yeah.data.repository.PracticeRepository
import com.yeah.domain.model.Lesson
import com.yeah.utils.ISharedPreferencesManager
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class PracticeRepositoryInstrumentedTest {

    private lateinit var db: AppDatabase
    private lateinit var lessonDao: LessonDao
    private lateinit var lessonApi: LessonApi
    private lateinit var sharedPreferencesManager: ISharedPreferencesManager
    private lateinit var practiceRepository: PracticeRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        lessonDao = db.lessonDao()
        lessonApi = mock(LessonApi::class.java) // Мокаем API
        sharedPreferencesManager = FakeSharedPreferencesManager() // Используем фейковую реализацию
        practiceRepository = PracticeRepository(lessonApi, lessonDao, sharedPreferencesManager)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun fetchAndSaveLesson() = runBlocking {
        // Arrange: Создаем тестовые данные
        val lesson = Lesson(
            id = 1,
            title = "Маневрирование в пределах перекрёстка",
            date = LocalDateTime.of(2024, 11, 20, 10, 0, 0, 0),
            lessonStatus = "назначено",
            studentName = "Иванов Иван",
            teacherName = "Петров Петр",
            description = "Практическое занятие по маневрированию на перекрёстке.",
            category = "Вождение",
            teacherPhoneNumber = "+7 999 123 45 67",
            studentPhoneNumber = "+7 999 765 43 21",
            teacherEmail = "teacher@example.com",
            studentEmail = "student@example.com"
        )

        // Act: Сохраняем урок через репозиторий
        practiceRepository.create(lesson)

        // Извлекаем урок из базы данных
        val dbLesson = lessonDao.getLessonById(1)

        // Assert: Проверяем сохранение в базе данных
        assertEquals(lesson.title, dbLesson?.title)
        assertEquals(lesson.teacherName, dbLesson?.teacherName)
    }
}
