package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ar.edu.itba.pam.nearchatter.db.room.NearchatterDb
import ar.edu.itba.pam.nearchatter.db.room.conversation.ConversationDao
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.SharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.models.Device
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.*
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.services.NearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.AndroidSchedulerProvider
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import java.util.function.Consumer

class NearchatterModule(context: Context) {
    private val applicationContext: Context = context.applicationContext;

    fun getApplicationContext(): Context {
        return applicationContext;
    }

    fun provideUserRepository(
        userDao: UserDao,
        conversationDao: ConversationDao,
        userMapper: UserMapper,
        conversationMapper: ConversationMapper
    ): IUserRepository {
        return UserRepository(userDao, conversationDao, userMapper, conversationMapper)
    }

    fun provideNearbyRepository(
        userRepository: UserRepository,
        disconnectedDeviceCallback: Consumer<Device>,
        connectedDeviceCallback: Consumer<Device>,
        messageCallback: Consumer<Message>,
    ): INearbyRepository {
        return NearbyRepository(
            getApplicationContext(),
            userRepository,
            disconnectedDeviceCallback,
            connectedDeviceCallback,
            messageCallback
        )
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
        view: PeersView,
        userRepository: IUserRepository,
        sharedPreferencesStorage: ISharedPreferencesStorage,
        schedulerProvider: SchedulerProvider,
        nearbyService: INearbyService,
        hwid: String
    ): PeersPresenter {
        return PeersPresenter(
            view,
            userRepository,
            sharedPreferencesStorage,
            schedulerProvider,
            nearbyService,
            hwid
        )
    }

    fun provideNearbyService(): INearbyService {
        return NearbyService(applicationContext)
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

    fun provideUserDao(): UserDao {
        return NearchatterDb.getInstance(getApplicationContext())?.userDao()!!
    }

    fun provideSchedulerProvider(): SchedulerProvider {
        return AndroidSchedulerProvider()
    }

    fun provideConversationMapper(): ConversationMapper {
        return ConversationMapper()
    }

    fun provideConversationDao(): ConversationDao {
        return NearchatterDb.getInstance(getApplicationContext())?.conversationDao()!!
    }

}
