package com.yeah.data.api.model

import java.time.LocalDateTime

data class LessonRequest(
    val title: String,              // Тема занятия
    val date: String,               // Дата и время занятия в виде строки (формат ISO 8601)
    val lessonStatus: String,       // Статус занятия: назначено, проведено, отменено
    val studentName: String,        // Имя ученика
    val teacherName: String,        // Имя учителя
    val description: String         // Описание занятия
)
