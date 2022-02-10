package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.login.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.login.repository.UserRepository

class NearchatterModule(context: Context) {
    private val applicationContext: Context = context.applicationContext;

    fun getApplicationContext(): Context {
        return applicationContext;
    }

    fun provideUserRepository(): IUserRepository {
        return UserRepository()
    }

    fun provideLoginPresenter(view: LoginView, userRepository: UserRepository): LoginPresenter {
        return LoginPresenter(view,userRepository);
    }

}