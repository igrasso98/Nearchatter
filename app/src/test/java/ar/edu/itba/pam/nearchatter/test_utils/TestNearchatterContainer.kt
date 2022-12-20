package ar.edu.itba.pam.nearchatter.test_utils

import android.content.Context
import ar.edu.itba.pam.nearchatter.chat.ChatPresenter
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.di.NearchatterContainer
import ar.edu.itba.pam.nearchatter.di.TestNearchatterModule
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

class TestNearchatterContainer() : NearchatterContainer {
    private val nearchatterModule: TestNearchatterModule = TestNearchatterModule()
    private var schedulerProvider: SchedulerProvider? = null

    override fun getApplicationContext(): Context {
        TODO("Not yet implemented")
    }

    override fun getHwId(): String {
        TODO("Not yet implemented")
    }

    override fun getUserRepository(): IUserRepository {
        TODO("Not yet implemented")
    }

    override fun getLoginPresenter(view: LoginView): LoginPresenter {
        TODO("Not yet implemented")
    }

    override fun getPeersPresenter(): PeersPresenter {
        TODO("Not yet implemented")
    }

    override fun getChatPresenter(userId: String): ChatPresenter {
        TODO("Not yet implemented")
    }

    override fun getNearbyService(): INearbyService {
        TODO("Not yet implemented")
    }

    override fun getSharedPreferencesStorage(): ISharedPreferencesStorage {
        TODO("Not yet implemented")
    }

    override fun getConnectionsClient(): ConnectionsClient {
        TODO("Not yet implemented")
    }

    override fun getMessageRepository(): IMessageRepository {
        TODO("Not yet implemented")
    }

    override fun getNearbyRepository(): INearbyRepository {
        TODO("Not yet implemented")
    }

    override fun getNearbyConnectionHandler(): INearbyConnectionHandler {
        TODO("Not yet implemented")
    }

    override fun getSchedulerProvider(): SchedulerProvider {
        if (this.schedulerProvider == null) {
            this.schedulerProvider = this.nearchatterModule.provideSchedulerProvider()
        }
        return schedulerProvider!!
    }

}
