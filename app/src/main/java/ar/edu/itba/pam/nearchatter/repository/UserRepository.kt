package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.domain.Conversation
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import io.reactivex.Flowable
import io.reactivex.Single

class UserRepository(
    private val userDao: UserDao,
    private val userMapper: UserMapper,
    private val conversationMapper: ConversationMapper,
) :
    IUserRepository {

    private var conversations: Flowable<List<Conversation>>? = null

    override fun addUser(user: User): Single<Unit> {
        return Single.fromCallable { userDao.insert(userMapper.toEntity(user)) }
    }

    override fun getUsernameById(id: String): Single<String> {
        return Single.fromCallable { userDao.getUsernameById(id) }
    }

    override fun updateUsername(userId: String, username: String): Single<Unit> {
        return Single.fromCallable { userDao.updateUser(userId, username) }
    }

    override fun getUserConversations(): Flowable<List<Conversation>> {
        if (this.conversations == null) {
            val myConversations = userDao.getAllConversations()
            conversations = myConversations.map(conversationMapper::fromUserConversations)
        }
        return this.conversations!!
    }

    override fun setLastMessage(message: Message): Single<Unit> {
        return Single.fromCallable { userDao.setLastMessage(message.getSenderId(), message.getReceiverId(), message.getId()!!) }
    }
}
