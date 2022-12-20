package ar.edu.itba.pam.nearchatter.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.provider.Settings
import ar.edu.itba.pam.nearchatter.chat.ChatPresenter
import ar.edu.itba.pam.nearchatter.db.room.NearchatterDb
import ar.edu.itba.pam.nearchatter.db.room.message.MessageDao
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.SharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.repository.*
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.services.NearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.AndroidSchedulerProvider
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.ConnectionsClient

class NearchatterModule(context: Context) {
    private val applicationContext: Context = context.applicationContext;

    fun getApplicationContext(): Context {
        return applicationContext;
    }

    @SuppressLint("HardwareIds")
    fun getHwId(): String {
        return Settings.Secure.getString(
            getApplicationContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun provideUserRepository(
        userDao: UserDao,
        userMapper: UserMapper,
        conversationMapper: ConversationMapper
    ): IUserRepository {
        return UserRepository(userDao, userMapper, conversationMapper)
    }

    fun provideNearbyRepository(
        hwId: String,
        nearbyConnectionHandler: INearbyConnectionHandler,
        connectionsClient: ConnectionsClient,
    ): INearbyRepository {
        return NearbyRepository(hwId, nearbyConnectionHandler, connectionsClient)
    }

    fun provideConnectionsClient(context: Context): ConnectionsClient {
        return Nearby.getConnectionsClient(context)
    }

    fun provideNearbyConnectionHandler(hwId: String): INearbyConnectionHandler {
        return NearbyConnectionHandler(hwId)
    }

    fun provideMessageRepository(
        messageDao: MessageDao,
        messageMapper: MessageMapper
    ): IMessageRepository {
        return MessageRepository(messageDao, messageMapper)
    }

    fun provideMessageMapper(): MessageMapper {
        return MessageMapper()
    }

    fun provideLoginPresenter(
        view: LoginView,
        userRepository: IUserRepository,
        sharedPreferencesStorage: ISharedPreferencesStorage,
        schedulerProvider: SchedulerProvider,
        hwid: String
    ): LoginPresenter {
        return LoginPresenter(
            view,
            userRepository,
            sharedPreferencesStorage,
            schedulerProvider,
            hwid
        )
    }

    fun providePeersPresenter(
        userRepository: IUserRepository,
        sharedPreferencesStorage: ISharedPreferencesStorage,
        schedulerProvider: SchedulerProvider,
        nearbyService: INearbyService,
        hwid: String
    ): PeersPresenter {
        return PeersPresenter(
            userRepository,
            sharedPreferencesStorage,
            schedulerProvider,
            nearbyService,
            hwid
        )
    }

    fun provideChatPresenter(
        userId: String,
        userRepository: IUserRepository,
        messageRepository: IMessageRepository,
        schedulerProvider: SchedulerProvider,
        nearbyService: INearbyService,
        hwId: String
    ): ChatPresenter {
        return ChatPresenter(
            userId,
            userRepository,
            messageRepository,
            schedulerProvider,
            nearbyService,
            hwId
        )
    }

    fun provideNearbyService(
        hwId: String,
        nearbyRepository: INearbyRepository,
        userRepository: IUserRepository,
        messageRepository: IMessageRepository,
        schedulerProvider: SchedulerProvider,
    ): INearbyService {
        return NearbyService(
            hwId,
            nearbyRepository,
            userRepository,
            messageRepository,
            schedulerProvider,
        )
    }

    fun provideSharedPreferencesStorage(): ISharedPreferencesStorage {
        return SharedPreferencesStorage(
            getApplicationContext().getSharedPreferences(
                "pam-pref",
                MODE_PRIVATE
            )
        )
    }

    fun provideUserMapper(): UserMapper {
        return UserMapper()
    }

    fun provideMessageDao(): MessageDao {
        return NearchatterDb.getInstance(getApplicationContext())?.messageDao()!!
    }

    fun provideUserDao(): UserDao {
        return NearchatterDb.getInstance(getApplicationContext())?.userDao()!!
    }

    fun provideSchedulerProvider(): SchedulerProvider {
        return AndroidSchedulerProvider()
    }

    fun provideConversationMapper(): ConversationMapper {
        return ConversationMapper()
    }
}
