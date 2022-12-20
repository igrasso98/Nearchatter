package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.chat.ChatPresenter
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.peers.PeersPresenter
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.INearbyConnectionHandler
import ar.edu.itba.pam.nearchatter.repository.INearbyRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import com.google.android.gms.nearby.connection.ConnectionsClient

interface NearchatterContainer {
    fun getApplicationContext(): Context
    fun getHwId(): String
    fun getUserRepository(): IUserRepository
    fun getLoginPresenter(view: LoginView): LoginPresenter
    fun getPeersPresenter(): PeersPresenter
    fun getChatPresenter(userId: String): ChatPresenter
    fun getNearbyService(): INearbyService
    fun getSharedPreferencesStorage(): ISharedPreferencesStorage
    fun getSchedulerProvider(): SchedulerProvider
    fun getConnectionsClient(): ConnectionsClient
    fun getMessageRepository(): IMessageRepository
    fun getNearbyRepository(): INearbyRepository
    fun getNearbyConnectionHandler(): INearbyConnectionHandler
}
