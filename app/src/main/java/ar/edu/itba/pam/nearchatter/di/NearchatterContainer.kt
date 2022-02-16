package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import android.content.SharedPreferences
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider

interface NearchatterContainer {
    fun getApplicationContext(): Context
    fun getUserRepository(): IUserRepository
    fun getLoginPresenter(view: LoginView): LoginPresenter
    fun getPeersPresenter(view: PeersView): PeersPresenter
    fun getNearbyService(): INearbyService
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
    fun getSchedulerProvider(): SchedulerProvider
}