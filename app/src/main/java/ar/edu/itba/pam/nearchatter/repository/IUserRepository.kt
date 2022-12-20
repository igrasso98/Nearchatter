package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun addUser(user: User): Single<Unit>

    fun setConnected(userId: String, connected: Boolean): Single<Unit>

    fun getUsernameById(id: String): Single<String?>

    fun updateUsername(userId: String, username: String): Single<Unit>

    fun getConnectedUserIds(): Flow<Set<String>>

    fun getUserConversations(): Flow<List<Conversation>>

    fun setLastMessage(message: Message): Single<Unit>
}
