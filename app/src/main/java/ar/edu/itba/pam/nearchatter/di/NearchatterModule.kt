package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.login.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.login.repository.UserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.services.NearbyService

class NearchatterModule(context: Context) {
    private val applicationContext: Context = context.applicationContext;

    fun getApplicationContext(): Context {
        return applicationContext;
    }

    fun provideUserRepository(): IUserRepository {
        return UserRepository()
    }

    fun provideLoginPresenter(view: LoginView, userRepository: UserRepository): LoginPresenter {
        return LoginPresenter(view, userRepository);
    }

    fun provideNearbyService(): INearbyService {
        return NearbyService(applicationContext)
    }

}