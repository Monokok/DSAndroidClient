package com.yeah.dsapp.ui.dashboard

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.yeah.dsapp.R
import com.yeah.domain.model.Lesson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.time.format.DateTimeFormatter

class LessonsAdapter(private var lessons: List<Lesson>,
                     private val userRole: String?,
                     private val lessonViewModel: LessonsViewModel // Передаем ViewModel в адаптер
) : RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    // ViewHolder для отдельного элемента
    class LessonViewHolder(itemView: View, private val lessonViewModel: LessonsViewModel) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.tv_lesson_title)
        private val dateTimeTextView: TextView = itemView.findViewById(R.id.tv_lesson_datetime)
        private val teacherFullNameTextView: TextView = itemView.findViewById(R.id.tv_teacher_full_name)
        private val teacherNumberTextView: TextView = itemView.findViewById(R.id.tv_teacher_number)
        private val lessonDescriptionTextView: TextView = itemView.findViewById(R.id.tv_lesson_description)
        private val lessonStatusTextView: TextView = itemView.findViewById(R.id.tv_lesson_status)
        private val teacherEmailTextView: TextView = itemView.findViewById(R.id.tv_teacher_email)

        // Элемент, содержащий кнопку "Отмена"
        private val cancelButtonContainer: View = itemView.findViewById(R.id.btn_cancel_container)
        private val cancelButton: Button = itemView.findViewById(R.id.btn_cancel)


        // Метод для связывания данных с элементами пользовательского интерфейса
        fun bind(lesson: Lesson, userRole: String?) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

            if (userRole == "teacher") {
                teacherFullNameTextView.text = "Обучающийся: ${lesson.studentName}"
                teacherNumberTextView.text = lesson.studentPhoneNumber.replace("\n", "").replace("\r", "")

                dateTimeTextView.text = "Дата и время: ${lesson.date.format(formatter)}"

                teacherEmailTextView.text = lesson.studentEmail

                lessonDescriptionTextView.text = lesson.description
            }
            else if (userRole == "student"){
                titleTextView.text = lesson.title
                dateTimeTextView.text = "Дата и время: ${lesson.date.format(formatter)}"
                teacherFullNameTextView.text = "Инструктор: ${lesson.teacherName}"
                teacherNumberTextView.text = lesson.teacherPhoneNumber.replace("\n", "").replace("\r", "")


                lessonDescriptionTextView.text = "Описание: ${lesson.description ?: "Не указано"}"
                teacherEmailTextView.text = lesson.teacherEmail
            }
            // Показываем или скрываем кнопку "Отмена" в зависимости от статуса
            if (lesson.lessonStatus == "Назначено") {
                cancelButtonContainer.visibility = View.VISIBLE // Показываем кнопку
            } else {
                cancelButtonContainer.visibility = View.GONE // Скрываем кнопку
            }

            cancelButton.setOnClickListener {
                // Передаем в метод логики, которая должна быть выполнена при нажатии
                Toast.makeText(itemView.context, "Отправка запроса отмены", Toast.LENGTH_SHORT).show()
                lessonViewModel.tryCancelLesson(lesson.id) { result ->
                    // Выводим результат операции, например через Toast
                    Toast.makeText(itemView.context, result, Toast.LENGTH_SHORT).show()

                    if (result == "Занятие успешно отменено") {  // Предположим, что этот результат говорит о успехе
                        //lesson.lessonStatus = "Отменено"
                        lessonViewModel.fetchLessons()
                    }

                }

            }
            lessonStatusTextView.text = lesson.lessonStatus




        }
    }

    // Создание новых ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_item, parent, false)
        return LessonViewHolder(view, lessonViewModel)//передача модели из внешнего класса во внутренний
    }

    // Привязка данных к ViewHolder
    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {

        holder.bind(lessons[position], userRole)
    }

    // Общее количество элементов в списке
    override fun getItemCount(): Int = lessons.size
    // Метод для обновления списка и уведомления об изменениях
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newLessons: List<Lesson>) {
        lessons = newLessons
        notifyDataSetChanged()  // Можно заменить на более оптимальный подход с DiffUtil
    }
}