package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import ar.edu.itba.pam.nearchatter.db.room.NearchatterDb
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.SharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.repository.UserMapper
import ar.edu.itba.pam.nearchatter.repository.UserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.services.NearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.AndroidSchedulerProvider
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider

class NearchatterModule(context: Context) {
    private val applicationContext: Context = context.applicationContext;

    fun getApplicationContext(): Context {
        return applicationContext;
    }

    fun provideUserRepository(
        userDao: UserDao,
        mapper: UserMapper
    ): IUserRepository {
        return UserRepository(userDao, mapper)
    }

    fun provideLoginPresenter(
        view: LoginView,
        userRepository: IUserRepository,
        sharedPreferencesStorage: ISharedPreferencesStorage,
        schedulerProvider: SchedulerProvider
    ): LoginPresenter {
        return LoginPresenter(view, userRepository, sharedPreferencesStorage, schedulerProvider)
    }

    fun providePeersPresenter(view: PeersView, userRepository: IUserRepository): PeersPresenter {
        return PeersPresenter(view, userRepository)
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
}