package ar.edu.itba.pam.nearchatter.db.room.user

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE user_id = :id")
    fun getById(id: Long): UserEntity

    @Insert
    fun insert(user: UserEntity): Long

    @Query("UPDATE users SET username = :username WHERE user_id = :id")
    fun updateUser(id: Long, username: String): Unit

//    @Delete
//    fun delete(user: UserEntity)
}