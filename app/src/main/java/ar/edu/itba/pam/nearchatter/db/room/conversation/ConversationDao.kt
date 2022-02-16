package ar.edu.itba.pam.nearchatter.db.room.conversation

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface ConversationDao {
    @Query(
        "SELECT * \n" +
                "FROM users \n" +
                "INNER JOIN conversations\n" +
                "      ON users.user_id=conversations.other_id\n" +
                "INNER JOIN messages\n" +
                "      ON messages.message_id=conversations.last_message_id"
    )
    fun getAll(): Flowable<List<UserConversation>>

    @Insert
    fun insert(vararg conversation: ConversationEntity)

    @Delete
    fun delete(conversation: ConversationEntity)
}