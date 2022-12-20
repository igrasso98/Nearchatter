package ar.edu.itba.pam.nearchatter.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserRepository(
    private val userDao: UserDao,
    private val userMapper: UserMapper,
    private val conversationMapper: ConversationMapper,
) :
    IUserRepository {

    private val connectedUsers: MutableLiveData<MutableSet<String>> = MutableLiveData(HashSet())

    override fun addUser(user: User): Single<Unit> {
        return Single.fromCallable { userDao.insertOrUpdateUsername(userMapper.toEntity(user)) }
    }

    override fun setConnected(userId: String, connected: Boolean): Single<Unit> {
        return Single.fromCallable {
            if (connected) {
                connectedUsers.value?.add(userId)
            } else {
                connectedUsers.value?.remove(userId)
            }
            // Update the value to trigger the flow
            connectedUsers.postValue(connectedUsers.value)
        }
    }

    override fun getUsernameById(id: String): Single<String?> {
        return Single.fromCallable { userDao.getUsernameById(id) }
    }

    override fun updateUsername(userId: String, username: String): Single<Unit> {
        return Single.fromCallable { userDao.updateUser(userId, username) }
    }

    override fun getConnectedUserIds(): Flow<Set<String>> {
        return connectedUsers.asFlow().map { it.toSet() }
    }

    override fun getUserConversations(): Flow<List<Conversation>> {
        val conversations = MutableLiveData<List<Conversation>>()
        userDao.getAllConversations().observeForever {
            conversations.postValue(it.map(conversationMapper::fromUserConversation))
        }
        return conversations.asFlow()
    }

    override fun setLastMessage(message: Message): Single<Unit> {
        return Single.fromCallable {
            userDao.setLastMessage(
                message.getSenderId(),
                message.getReceiverId(),
                message.getId()
            )
        }
    }
}
