package ar.edu.itba.pam.nearchatter.di

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import ar.edu.itba.pam.nearchatter.db.room.NearchatterDb
import ar.edu.itba.pam.nearchatter.db.room.conversation.ConversationDao
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.ConversationMapper
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.repository.UserMapper
import ar.edu.itba.pam.nearchatter.repository.UserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider

class ProductionNearchatterContainer(context: Context) : NearchatterContainer {
    private val nearchatterModule: NearchatterModule = NearchatterModule(context)
    private var userRepository: IUserRepository? = null
    private var userMapper: UserMapper? = null
    private var conversationMapper: ConversationMapper? = null
    private var userDao: UserDao? = null
    private var conversationDao: ConversationDao? = null
    private var loginPresenter: LoginPresenter? = null
    private var peersPresenter: PeersPresenter? = null
    private var nearbyService: INearbyService? = null
    private var sharedPreferencesStorage: ISharedPreferencesStorage? = null
    private var schedulerProvider: SchedulerProvider? = null

    override fun getApplicationContext(): Context {
        return nearchatterModule.getApplicationContext()
    }

    override fun getUserRepository(): IUserRepository {
        if (this.userRepository == null) {
            this.userRepository = this.nearchatterModule.provideUserRepository(
                getUserDao(),
                getConversationDao(),
                getUserMapper(),
                getConversationMapper(),
            )
        }
        return this.userRepository!!
    }

    @SuppressLint("HardwareIds")
    override fun getLoginPresenter(view: LoginView): LoginPresenter {
        if (this.loginPresenter == null) {
            this.loginPresenter = this.nearchatterModule.provideLoginPresenter(
                view,
                getUserRepository(),
                getSharedPreferencesStorage(),
                getSchedulerProvider(),
                Settings.Secure.getString(
                    getApplicationContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
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

    @SuppressLint("HardwareIds")
    override fun getPeersPresenter(view: PeersView): PeersPresenter {
        if (this.peersPresenter == null) {
            this.peersPresenter = this.nearchatterModule.providePeersPresenter(
                view,
                getUserRepository(),
                getSharedPreferencesStorage(),
                getSchedulerProvider(),
                getNearbyService(),
                Settings.Secure.getString(
                    getApplicationContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
        }
        return this.peersPresenter!!
    }

    override fun getNearbyService(): INearbyService {
        if (this.nearbyService == null) {
            this.nearbyService = this.nearchatterModule.provideNearbyService()
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

    private fun getConversationMapper(): ConversationMapper {
        if (this.conversationMapper == null) {
            this.conversationMapper = this.nearchatterModule.provideConversationMapper()
        }
        return this.conversationMapper!!
    }

    private fun getConversationDao(): ConversationDao {
        if (this.conversationDao == null) {
            conversationDao = this.nearchatterModule.provideConversationDao()
        }
        return conversationDao!!
    }


}