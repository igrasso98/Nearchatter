package ar.edu.itba.pam.nearchatter.repository

import androidx.lifecycle.MutableLiveData
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import kotlinx.coroutines.flow.Flow
import io.reactivex.Single

interface IUserRepository {
    fun addUser(user: User): Single<Unit>

    fun getUsernameById(id: String): Single<String>

    fun updateUsername(userId: String, username: String): Single<Unit>

    fun getUserConversations(): Flow<List<Conversation>>

    fun setLastMessage(message: Message): Single<Unit>
}
