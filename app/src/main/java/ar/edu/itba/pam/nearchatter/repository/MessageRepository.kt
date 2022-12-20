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
    override fun addMessage(message: Message): Single<Unit> {
        return Single.fromCallable { messageDao.insert(messageMapper.toEntity(message)) }
    }

    override fun getMessagesById(userId: String): Flow<List<Message>> {
        val messages = MutableLiveData<List<Message>>()
        messageDao.getById(userId).observeForever {
            messages.postValue(it.map(messageMapper::fromEntity))
        }
        return messages.asFlow()
    }
}
