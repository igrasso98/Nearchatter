package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.peers.PeersView
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.INearbyRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider

interface NearchatterContainer {
    fun getApplicationContext(): Context
    fun getHwId(): String
    fun getUserRepository(): IUserRepository
    fun getLoginPresenter(view: LoginView): LoginPresenter
    fun getPeersPresenter(view: PeersView): PeersPresenter
    fun getNearbyService(): INearbyService
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
    fun getSchedulerProvider(): SchedulerProvider
    fun getMessageRepository(): IMessageRepository
    fun getNearbyRepository(): INearbyRepository
}
