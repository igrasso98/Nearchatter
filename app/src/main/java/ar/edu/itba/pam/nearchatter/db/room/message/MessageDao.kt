package ar.edu.itba.pam.nearchatter.db.room.message

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages")
    fun getAll(): List<MessageEntity>

    @Insert
    fun insert(vararg message: MessageEntity)

    @Delete
    fun delete(message: MessageEntity)
}