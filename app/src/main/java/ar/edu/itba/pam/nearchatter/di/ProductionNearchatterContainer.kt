package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.db.room.NearchatterDb
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.repository.UserMapper
import ar.edu.itba.pam.nearchatter.repository.UserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider

class ProductionNearchatterContainer(context: Context) : NearchatterContainer {
    private val nearchatterModule: NearchatterModule = NearchatterModule(context)
    private var userRepository: IUserRepository? = null
    private var userMapper: UserMapper? = null
    private var userDao: UserDao? = null
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
                getUserDao(), getUserMapper()
            )
        }
        return this.userRepository!!
    }

    override fun getLoginPresenter(view: LoginView): LoginPresenter {
        if (this.loginPresenter == null) {
            this.loginPresenter = this.nearchatterModule.provideLoginPresenter(
                view,
                getUserRepository(),
                getSharedPreferencesStorage(),
                getSchedulerProvider(),
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
                getUserRepository()
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


}