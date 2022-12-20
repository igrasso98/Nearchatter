package ar.edu.itba.pam.nearchatter.db.room.message

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages")
    fun getAll(): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE sender_id == :id OR receiver_id == :id")
    fun getById(id: String): LiveData<List<MessageEntity>>

    @Insert
    fun insert(message: MessageEntity)

    @Delete
    fun delete(message: MessageEntity)
}
