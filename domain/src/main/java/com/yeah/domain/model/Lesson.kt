package com.yeah.domain.model

import java.time.LocalDateTime

data class Lesson(
    val id: Int,
    val title: String, // тема. Н-р меневрирование в пределах перекрестка
    val date: LocalDateTime,
    var lessonStatus: String, // назначено, проведено, отменено
    val studentName: String,
    val teacherName: String,
    val description: String,
    val category: String,
    val teacherPhoneNumber: String,
    val studentPhoneNumber: String,
    val teacherEmail: String,
    val studentEmail: String,
    )