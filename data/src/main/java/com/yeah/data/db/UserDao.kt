package com.yeah.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yeah.data.db.model.UserDbo //User из db.model

//Интерфейс DAO для пользователя
@Dao
interface UserDao {
    @Insert
    suspend fun insert(userDbo: UserDbo)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserDbo?

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserDbo?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserDbo>

    @Update
    suspend fun updateUser(userDbo: UserDbo);

    @Delete
    suspend fun deleteUser(userDbo: UserDbo)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: String)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUserById(id: Int)

    @Query("DELETE FROM users")
    suspend fun clearUsers() // Метод для удаления всех пользователей

}
