package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.chat.ChatPresenter
import ar.edu.itba.pam.nearchatter.chat.ChatView
import ar.edu.itba.pam.nearchatter.db.room.message.MessageDao
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.*
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import com.google.android.gms.nearby.connection.ConnectionsClient

class ProductionNearchatterContainer(context: Context) : NearchatterContainer {
    private val nearchatterModule: NearchatterModule = NearchatterModule(context)
    private var userRepository: IUserRepository? = null
    private var userMapper: UserMapper? = null
    private var conversationMapper: ConversationMapper? = null
    private var userDao: UserDao? = null
    private var loginPresenter: LoginPresenter? = null
    private var peersPresenter: PeersPresenter? = null
    private var chatPresenter: ChatPresenter? = null
    private var nearbyService: INearbyService? = null
    private var messageRepository: IMessageRepository? = null
    private var messageDao: MessageDao? = null
    private var messageMapper: MessageMapper? = null
    private var connectionsClient: ConnectionsClient? = null
    private var nearbyRepository: INearbyRepository? = null
    private var nearbyConnectionHandler: INearbyConnectionHandler? = null
    private var sharedPreferencesStorage: ISharedPreferencesStorage? = null
    private var schedulerProvider: SchedulerProvider? = null

    override fun getApplicationContext(): Context {
        return nearchatterModule.getApplicationContext()
    }

    override fun getHwId(): String {
        return nearchatterModule.getHwId()
    }

    override fun getUserRepository(): IUserRepository {
        if (this.userRepository == null) {
            this.userRepository = this.nearchatterModule.provideUserRepository(
                getUserDao(),
                getUserMapper(),
                getConversationMapper(),
            )
        }
        return this.userRepository!!
    }

    override fun getMessageRepository(): IMessageRepository {
        if (this.messageRepository == null) {
            this.messageRepository = this.nearchatterModule.provideMessageRepository(
                getMessageDao(),
                getMessageMapper(),
            )
        }
        return this.messageRepository!!;
    }

    override fun getConnectionsClient(): ConnectionsClient {
        if (this.connectionsClient == null) {
            this.connectionsClient = this.nearchatterModule.provideConnectionsClient(
                getApplicationContext()
            )
        }
        return this.connectionsClient!!;
    }

    override fun getNearbyRepository(): INearbyRepository {
        if (this.nearbyRepository == null) {
            this.nearbyRepository = this.nearchatterModule.provideNearbyRepository(
                getHwId(),
                getNearbyConnectionHandler(),
                getConnectionsClient(),
            )
        }
        return this.nearbyRepository!!;
    }

    override fun getNearbyConnectionHandler(): INearbyConnectionHandler {
        if (this.nearbyConnectionHandler == null) {
            this.nearbyConnectionHandler = this.nearchatterModule.provideNearbyConnectionHandler(
                getHwId(),
            )
        }
        return this.nearbyConnectionHandler!!;
    }

    override fun getLoginPresenter(view: LoginView): LoginPresenter {
        if (this.loginPresenter == null) {
            this.loginPresenter = this.nearchatterModule.provideLoginPresenter(
                view,
                getUserRepository(),
                getSharedPreferencesStorage(),
                getSchedulerProvider(),
                getHwId(),
            )
        }
        return this.loginPresenter!!
    }

    override fun getSchedulerProvider(): SchedulerProvider {
        if (this.schedulerProvider == null) {
            this.schedulerProvider = this.nearchatterModule.provideSchedulerProvider()
        }
        return schedulerProvider!!
    }

    override fun getPeersPresenter(view: PeersView): PeersPresenter {
        if (this.peersPresenter == null) {
            this.peersPresenter = this.nearchatterModule.providePeersPresenter(
                view,
                getUserRepository(),
                getSharedPreferencesStorage(),
                getSchedulerProvider(),
                getNearbyService(),
                getHwId(),
            )
        }
        return this.peersPresenter!!
    }

    override fun getChatPresenter(view: ChatView, userId: String): ChatPresenter {
        if (this.chatPresenter == null) {
            this.chatPresenter = this.nearchatterModule.provideChatPresenter(
                view,
                userId,
                getUserRepository(),
                getMessageRepository(),
                getSchedulerProvider(),
                getNearbyService(),
                getHwId(),
            )
        }
        return this.chatPresenter!!
    }

    override fun getNearbyService(): INearbyService {
        if (this.nearbyService == null) {
            this.nearbyService = this.nearchatterModule.provideNearbyService(
                getNearbyRepository(),
                getUserRepository(),
                getMessageRepository(),
            )
        }
        return this.nearbyService!!
    }

    override fun getSharedPreferencesStorage(): ISharedPreferencesStorage {
        if (this.sharedPreferencesStorage == null) {
            this.sharedPreferencesStorage = this.nearchatterModule.provideSharedPreferencesStorage()
        }
        return this.sharedPreferencesStorage!!
    }

    private fun getUserMapper(): UserMapper {
        if (this.userMapper == null) {
            this.userMapper = this.nearchatterModule.provideUserMapper()
        }
        return this.userMapper!!
    }

    private fun getUserDao(): UserDao {
        if (this.userDao == null) {
            userDao = this.nearchatterModule.provideUserDao()
        }
        return userDao!!
    }

    private fun getMessageDao(): MessageDao {
        if (this.messageDao == null) {
            messageDao = this.nearchatterModule.provideMessageDao()
        }
        return messageDao!!
    }

    private fun getMessageMapper(): MessageMapper {
        if (this.messageMapper == null) {
            this.messageMapper = this.nearchatterModule.provideMessageMapper()
        }
        return this.messageMapper!!;
    }

    private fun getConversationMapper(): ConversationMapper {
        if (this.conversationMapper == null) {
            this.conversationMapper = this.nearchatterModule.provideConversationMapper()
        }
        return this.conversationMapper!!
    }
}
