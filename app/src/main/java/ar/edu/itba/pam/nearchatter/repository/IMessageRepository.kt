package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Message
import io.reactivex.Single

interface IMessageRepository {
    fun addMessage(message: Message): Single<Message>
}
