package com.yeah.data.api.model

data class LessonResponse(
    val id: Int,                    // Уникальный идентификатор занятия
    val title: String,              // Тема занятия
    val date: String,               // Дата и время занятия в виде строки
    val status: String,             // Статус занятия
    val studentName: String,        // Имя ученика
    val teacherName: String,        // Имя учителя
    val description: String,        // Описание занятия
    val teacherPhoneNumber: String,
    val studentPhoneNumber: String,
    val teacherEmail: String,
    val studentEmail: String,
    val stringDate: String,

)
