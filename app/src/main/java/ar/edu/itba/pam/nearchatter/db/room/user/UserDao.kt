package ar.edu.itba.pam.nearchatter.db.room.user

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE user_id = :id")
    fun getById(id: String): UserEntity

    @Query("SELECT username FROM users WHERE user_id = :id")
    fun getUsernameById(id: String): String

    @Insert
    fun insert(user: UserEntity)

    @Query("UPDATE users SET username = :username WHERE user_id = :id")
    fun updateUser(id: String, username: String)

//    @Delete
//    fun delete(user: UserEntity)
}