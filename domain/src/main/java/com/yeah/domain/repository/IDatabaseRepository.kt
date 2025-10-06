package com.yeah.domain.repository

import com.yeah.domain.model.Lesson
import com.yeah.domain.model.User

//интерфейс для взаимодействия с репозиториями UnitOfWork
public interface IDatabaseRepository{
    var users: IRepository<User>
    var lessons: IRepository<Lesson>

}