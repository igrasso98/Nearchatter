package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.login.repository.IUserRepository

interface NearchatterContainer {
    fun getApplicationContext(): Context
    fun getUserRepository(): IUserRepository
    fun getLoginPresenter(view: LoginView): LoginPresenter
}