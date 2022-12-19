package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun addUser(user: User): Single<Unit>

    fun getUsernameById(id: String): Single<String>

    suspend fun updateUsername(userId: String, username: String): Single<Unit>

    fun getUserConversations(): Flow<List<Conversation>>

    suspend fun setLastMessage(message: Message): Single<Unit>
}
