package com.yeah.data.services

import android.util.Log
import com.yeah.data.api.LessonApi
import com.yeah.data.api.model.LessonResponse
import com.yeah.data.db.DSConverters
import com.yeah.domain.model.ErrorMessage
import com.yeah.domain.model.Lesson
import com.yeah.domain.model.LoginResponse
import com.yeah.domain.model.RegisterResponse
import com.yeah.domain.model.User
import com.yeah.domain.services.IUserService
import com.yeah.domain.repository.IDatabaseRepository
import com.yeah.utils.ISharedPreferencesManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class UserService(private val db: IDatabaseRepository,
                  private val spManager: ISharedPreferencesManager,
                  private val lessonApi: LessonApi
): IUserService {

    override suspend fun login(email: String, password: String): LoginResponse? {
        return try {
            db.users.login(email, password);
        } catch (e: Exception){
            val msg: String = e.message.toString()

            LoginResponse(null, ErrorMessage(msg, "UserService login() method error"))
        }
    }

    override suspend fun getUserRoles(): List<String> {
        var user = getUser();
        if (user != null){
            // Удаляем квадратные скобки и разделяем строку по запятым
            val roles = user.userRole
                .removePrefix("[")
                .removeSuffix("]")
                .split(",")
                .map { it.trim().removeSurrounding("\"") }

            // Обработка списка строк
            val role = roles.map {
                when (it) {
                    "student" -> "Обучающийся"
                    "teacher" -> "Преподаватель"
                    else -> ""
                }
            }.filter { it.isNotEmpty() }.joinToString(" ")
            return roles;
        }
        else return emptyList();
    }

    override suspend fun getUser(): User? {
        return db.users.getList().firstOrNull();
    }

    override suspend fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        phoneNumber: String,
        email: String,
        password: String,
            passwordConfirm: String,
        role: String,
    ): RegisterResponse? {
        return try {
            db.users.register(
                firstName,
                lastName,
                middleName,
                phoneNumber, email,
                password, passwordConfirm, role);
        }
        catch (e: Exception){
            val msg: String = e.message.toString()
            RegisterResponse(null, listOf(msg));
        }
    }

    override suspend fun logout(): Boolean {
        db.lessons.clear();
        return db.users.logout() ?: false
    }

    //получение списка из преподавателей
    override suspend fun getTeachers(): List<User>{
        try {
            return lessonApi.getTeacher().map { teacherDTO ->
                User(
                    id = teacherDTO.id,
                    firstName = teacherDTO.first_name,
                    lastName = teacherDTO.last_name,
                    middleName = teacherDTO.middle_name,
                    userRole = "teacher",
                    paidHours = 0,
                    cHours = 0,
                    bHours = 0,
                    aHours = 0,
                    phoneNumber = teacherDTO.phoneNumber,
                    isAuthenticated = true,
                    email = teacherDTO.email
                )
            }
        }
        catch (e: Exception){
            Log.e("LessonsViewModel.UserService", "Не удалось запросить преподавателей: ${e.message}", e)
            return emptyList<User>()
        }
    }

    override suspend fun getStudents(): List<User> {
        try {
            val studentsResponse = lessonApi.getStudents();
            return studentsResponse.map { studentDTO ->
                User(
                    id = studentDTO.id,
                    firstName = studentDTO.first_name,
                    lastName = studentDTO.last_name,
                    middleName = studentDTO.middle_name,
                    userRole = "teacher",
                    paidHours = 0,
                    cHours = 0,
                    bHours = 0,
                    aHours = 0,
                    phoneNumber = studentDTO.phoneNumber,
                    isAuthenticated = true,
                    email = studentDTO.email
                )
            }
        }
        catch (e: Exception){
            Log.e("LessonsViewModel.UserService", "Не удалось запросить обучающихся: ${e.message}", e)
            return emptyList<User>()
        }
    }


    override suspend fun getTimes(teacherId: String, selectedDate: String): List<String> {
        return try{
            val times: List<String> = lessonApi.fetchAvailableTimes(teacherId, selectedDate)
            times //return
        }
        catch(e: Exception){
            Log.e("LessonsViewModel.UserService", "нет доступных вариантов времни для записи: ${e.message}", e)
            return emptyList<String>()
        }
    }

    override suspend fun createLesson(teacherId: String, date: String, time: String): Lesson? {
        try{
            val user = db.users.getList().firstOrNull()
            if (user != null){
                val responseLesson = lessonApi.createLesson(LessonApi.CreateLessonRequest(
                    dateTime = "$date $time",
                    studentId = user.id,
                    teacherId = teacherId,
                    category = "B",
                ))
                if (responseLesson != null){
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // Учитываем 'T'
                    val dateTime = LocalDateTime.parse(responseLesson.date, formatter)
                    val fromStringDate = parseStringDate(responseLesson.stringDate)
                    if (fromStringDate == null){
                        Log.d("LessonsViewModel.UserService", "Ошибка сохранения занятия в бд ${responseLesson.stringDate}")

                    }
                    val lsn = Lesson(
                        id = responseLesson.id,
                        title = responseLesson.title,
                        date = fromStringDate!!,
                        lessonStatus = responseLesson.status,
                        studentName = responseLesson.studentName,
                        teacherName = responseLesson.teacherName,
                        description = responseLesson.description,
                        category = "B",
                        teacherPhoneNumber = responseLesson.teacherPhoneNumber,
                        studentPhoneNumber = responseLesson.studentPhoneNumber,
                        teacherEmail = responseLesson.teacherEmail,
                        studentEmail = responseLesson.studentEmail,
                    )

                    db.lessons.create(lsn)
                    Log.i("LessonsViewModel.UserService", "Занятие с датой ${lsn.date}создано!")

                    return lsn
                }
                else return null
            }
            else return null
        }
        catch (e: Exception){
            e.message?.let {
                Log.e("LessonsViewModel.UserService", "не удалось создать занятие: ${e.message}", e)
            }
            return null
        }
    }

    override suspend fun createLessonByTeacher(studentId: String, date: String, time: String): Lesson? {
        try{
            val user = db.users.getList().firstOrNull()
            if (user != null){
                val responseLesson = lessonApi.createLesson(LessonApi.CreateLessonRequest(
                    dateTime = "$date $time",
                    studentId = studentId,
                    teacherId = user.id,
                    category = "B",
                ))
                if (responseLesson != null){
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // Учитываем 'T'
                    val dateTime = LocalDateTime.parse(responseLesson.date, formatter)
                    val fromStringDate = parseStringDate(responseLesson.stringDate)
                    if (fromStringDate == null){
                        Log.d("LessonsViewModel.UserService", "Ошибка сохранения занятия в бд ${responseLesson.stringDate}")

                    }
                    val lsn = Lesson(
                        id = responseLesson.id,
                        title = responseLesson.title,
                        date = fromStringDate!!,
                        lessonStatus = responseLesson.status,
                        studentName = responseLesson.studentName,
                        teacherName = responseLesson.teacherName,
                        description = responseLesson.description,
                        category = "B",
                        teacherPhoneNumber = responseLesson.teacherPhoneNumber,
                        studentPhoneNumber = responseLesson.studentPhoneNumber,
                        teacherEmail = responseLesson.teacherEmail,
                        studentEmail = responseLesson.studentEmail,
                    )

                    db.lessons.create(lsn)
                    Log.i("LessonsViewModel.UserService", "Занятие с датой ${lsn.date}создано!")

                    return lsn
                }
                else return null
            }
            else return null
        }
        catch (e: Exception){
            e.message?.let {
                Log.e("LessonsViewModel.UserService", "не удалось создать занятие: ${e.message}", e)
            }
            return null
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        //return db.users.getList().isEmpty()
        var token = spManager.getAuthToken()
        // Возвращаем true, если токен не равен null, иначе false
        return token != null
    }

    override suspend fun getLessons(): List<Lesson> {
        return try {
            val user = db.users.getList().firstOrNull();
            if (user != null) { //если есть userId
                //val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                //val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

                val remoteLessons: List<LessonResponse> = lessonApi.getLessonsByUserId(user.id) //            // 1. Получаем данные из удаленного источника


                if (remoteLessons.isEmpty()){
                    return db.lessons.getList().sortedByDescending { it.date }
                }
                // 2. Обновляем локальную базу данных
                db.lessons.clear(); // Очищаем старые данные, если это необходимо

                // Цикл для добавления занятий в локальную базу данных
                for (lessonResponse in remoteLessons) {
                    val date = parseStringDate(lessonResponse.stringDate)//LocalDateTime.parse(lessonResponse.date, dateFormatter)
                    Log.i("LessonsViewModel.UserService", "распаршенная дата: ${date}")
                    if (date == null) {
                        Log.d("LessonsViewModel.UserService", "не удалось распарсить дату: ${lessonResponse.stringDate}")
                    }
                    // Преобразуем LessonResponse в модель Lesson
                    val lesson = Lesson(
                        id = lessonResponse.id,
                        date = date!!,
                        title = lessonResponse.title,
                        description = lessonResponse.description,
                        category = "B",
                        lessonStatus = lessonResponse.status,
                        studentName = lessonResponse.studentName,
                        teacherName = lessonResponse.teacherName,
                        teacherPhoneNumber = lessonResponse.teacherPhoneNumber,
                        studentPhoneNumber = lessonResponse.studentPhoneNumber,
                        teacherEmail = lessonResponse.teacherEmail,
                        studentEmail = lessonResponse.studentEmail,
                    )

                    // Добавляем занятие в базу данных
                    try{
                        db.lessons.create(lesson)
                    }
                    catch (e: Exception){
                        Log.e("LessonsViewModel.UserService", "ошибка при создании урока на основе данных от сервера: ${e.message}", e)
                    }
                }

                // 3. Возвращаем обновленный список из локальной базы данных
                db.lessons.getList().sortedByDescending { it.date };//.map { it.toDomain() }
            }
            else return emptyList()
        } catch (e: Exception) {
            // Обработка ошибок (например, если сеть недоступна)
            // В случае ошибки возвращаем текущие данные из локальной базы данных
            Log.e("LessonsViewModel.UserService", "Ошибка получения данных: ${e.message}", e)
            db.lessons.getList();//lessonDao.getAllLessons().map { it.toDomain() }
        }
    }

    override suspend fun getLessonsFromDb(): List<Lesson> {
        return db.lessons.getList()
    }

    fun parseDateTime(dateTimeString: String): LocalDateTime? {
        val formatterWithMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val formatterWithoutMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

        return try {
            // Пытаемся распарсить строку с миллисекундами
            LocalDateTime.parse(dateTimeString, formatterWithMillis)
        } catch (e: DateTimeParseException) {
            Log.e("LessonsViewModel.UserService", "Формат с миллисекундами не подошел: ${e.message}")
            try {
                // Если не получилось, пробуем без миллисекунд
                LocalDateTime.parse(dateTimeString, formatterWithoutMillis)
            } catch (e: DateTimeParseException) {
                Log.e("LessonsViewModel.UserService", "Никакой формат не подошел: ${e.message}")
                null // Возвращаем null, если оба формата не подходят
            }
        }
    }

    fun parseStringDate(dateTimeString: String): LocalDateTime? {
        // Два возможных формата для парсинга
        val formatterWithTwoDigitsHours = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formatterWithOneOrTwoDigitsHours = DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm")

        return try {
            // Пытаемся распарсить строку с двузначными часами
            LocalDateTime.parse(dateTimeString, formatterWithTwoDigitsHours)
        } catch (e: DateTimeParseException) {
            //Log.e("LessonsViewModel.UserService", "Ошибка при парсинге с двумя цифрами для часов: ${e.message}", e)
            try {
                // Если не получилось, пробуем с одно- и двузначными часами
                LocalDateTime.parse(dateTimeString, formatterWithOneOrTwoDigitsHours)
            } catch (e: DateTimeParseException) {
                Log.e("LessonsViewModel.UserService", "Ошибка при парсинге с одним или двумя цифрами для часов: ${e.message}", e)
                null // Возвращаем null, если оба формата не подходят
            }
        }
    }

    //отправка запроса на отмену занятия
    override suspend fun tryCancelLesson(id: Int): String {
        try {
            lessonApi.tryCancelLesson(id);
        }
        catch (e: Exception){
            e.let { return it.message.toString()}
        }
        return "Занятие успешно отменено"
    }
}