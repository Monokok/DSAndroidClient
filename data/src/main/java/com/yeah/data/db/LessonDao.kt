package com.yeah.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yeah.data.db.model.LessonDbo


//04.11.2024 Прикрутить список занятий к фрагменту, чтоб был вывод
//сделать создание занятий по кнопке
@Dao
interface LessonDao {
    @Insert
    suspend fun insert(lessonsDbo: LessonDbo)

    @Query("SELECT * FROM lessons WHERE id = :id")
    suspend fun getLessonById(id: Int): LessonDbo?

    @Query("SELECT * FROM lessons")
    suspend fun getAllLessons(): List<LessonDbo>

    @Update
    suspend fun updateLesson(lessonDbo: LessonDbo);

    @Delete
    suspend fun deleteLesson(lessonDbo: LessonDbo)

    @Query("DELETE FROM lessons WHERE id = :id")
    suspend fun deleteLessonById(id: Int)

    @Query("DELETE FROM lessons")
    suspend fun clearLessons()

}
