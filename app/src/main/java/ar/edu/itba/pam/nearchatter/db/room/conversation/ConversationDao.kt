package ar.edu.itba.pam.nearchatter.db.room.conversation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ConversationDao {
    @Query("SELECT users.*, conversations.* FROM users INNER JOIN conversations ON users.user_id = conversation_id.")
    fun getAll(): List<UserConversation>

    @Insert
    fun insert(vararg conversation: ConversationEntity)

    @Delete
    fun delete(conversation: ConversationEntity)
}