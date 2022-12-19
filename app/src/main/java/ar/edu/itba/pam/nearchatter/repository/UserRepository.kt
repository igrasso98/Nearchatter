package ar.edu.itba.pam.nearchatter.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow


class UserRepository(
    private val userDao: UserDao,
    private val userMapper: UserMapper,
    private val conversationMapper: ConversationMapper,
) :
    IUserRepository {


    override suspend fun addUser(user: User): Single<Unit> {
        return Single.fromCallable { userDao.insert(userMapper.toEntity(user)) }
    }

    override fun getUsernameById(id: String): Single<String> {
        return Single.fromCallable { userDao.getUsernameById(id) }
    }

    override suspend fun updateUsername(userId: String, username: String): Single<Unit> {
        return Single.fromCallable { userDao.updateUser(userId, username) }
    }

    override fun getUserConversations(): Flow<List<Conversation>> {
        val conversations = MutableLiveData<List<Conversation>>()
        userDao.getAllConversations().observeForever {
            conversations.postValue(it.map(conversationMapper::fromUserConversation))
        }
        return conversations.asFlow()
    }


    override suspend fun setLastMessage(message: Message): Single<Unit> {
        return Single.fromCallable {
            userDao.setLastMessage(
                message.getSenderId(),
                message.getReceiverId(),
                message.getId()!!
            )
        }
    }
}
