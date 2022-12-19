package ar.edu.itba.pam.nearchatter.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ar.edu.itba.pam.nearchatter.db.room.message.MessageDao
import ar.edu.itba.pam.nearchatter.domain.Message
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

class MessageRepository(
    private val messageDao: MessageDao,
    private val messageMapper: MessageMapper,
) :
    IMessageRepository {
    override fun addMessage(message: Message): Single<Message> {
        return Single.fromCallable {
            println("Saving message: $message")
            val id = messageDao.insert(messageMapper.toEntity(message))
            println("Setting id: $id")
            messageMapper.setId(id, message)
        }
    }

    override fun getMessagesById(userId: String): Flow<List<Message>> {
        val messages = MutableLiveData<List<Message>>()
        messageDao.getById(userId).observeForever {
            messages.postValue(it.map(messageMapper::fromEntity))
        }
        return messages.asFlow()
    }
}
