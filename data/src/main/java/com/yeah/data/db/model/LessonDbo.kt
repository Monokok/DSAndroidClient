package com.yeah.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeah.domain.model.Lesson
import java.time.LocalDateTime

//dbo - Database object
@Entity(tableName = "lessons")
data class LessonDbo(
    @PrimaryKey val id: Int,
    val title: String, //тема. Н-р меневрирование в пределах перекрестка
    val date: LocalDateTime,
    val lessonStatus: String, //назначено, проведено, отменено
    val studentName: String,
    val teacherName: String,
    val description: String,
    val category: String,
    val teacherPhoneNumber: String,
    val studentPhoneNumber: String,
    val teacherEmail: String,
    val studentEmail: String,
)

fun Lesson.toLessonDbo(): LessonDbo{
    return LessonDbo(
        id,
        title,
        date,
        lessonStatus,
        studentName,
        teacherName,
        description,
        category,
        teacherPhoneNumber,
        studentPhoneNumber,
        teacherEmail,
        studentEmail,
    )
}

fun LessonDbo.toDomain(): Lesson{
    return Lesson(
        id,
        title,
        date,
        lessonStatus,
        studentName,
        teacherName,
        description,
        category,
        teacherPhoneNumber,
        studentPhoneNumber,
        teacherEmail,
        studentEmail,
    )
}
