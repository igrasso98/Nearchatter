package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.message.MessageDao
import ar.edu.itba.pam.nearchatter.domain.Message
import io.reactivex.Single

class MessageRepository(
    private val messageDao: MessageDao,
    private val messageMapper: MessageMapper,
) :
    IMessageRepository {
    override fun addMessage(message: Message): Single<Message> {
        return Single.fromCallable {
            val id = messageDao.insert(messageMapper.toEntity(message))
            messageMapper.setId(id, message)
        }
    }
}
