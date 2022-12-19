package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Message
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface IMessageRepository {
    fun addMessage(message: Message): Single<Message>

    fun getMessagesById(userId: String): Flow<List<Message>>
}
