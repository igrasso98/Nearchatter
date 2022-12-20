package ar.edu.itba.pam.nearchatter.db.room.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface UserDao {
    @Query(
        "SELECT * \n" +
                "FROM users \n" +
                "LEFT OUTER JOIN messages\n" +
                "      ON messages.message_id=last_message_id"
    )
    fun getAllConversations(): LiveData<List<UserConversation>>

    @Query("SELECT username FROM users WHERE user_id = :id")
    fun getUsernameById(id: String): String?

    @Insert
    fun insert(user: UserEntity)

    @Transaction
    fun insertOrUpdateUsername(user: UserEntity) {
        val username = getUsernameById(user.userId)
        if (username == null) {
            insert(user)
        } else {
            updateUser(user.userId, user.username)
        }
    }

    @Query("UPDATE users SET username = :username WHERE user_id = :id")
    fun updateUser(id: String, username: String)

    @Query("UPDATE users SET last_message_id = :message_id WHERE user_id = :sender_id OR user_id = :receiver_id")
    fun setLastMessage(sender_id: String, receiver_id: String, message_id: String)
}
