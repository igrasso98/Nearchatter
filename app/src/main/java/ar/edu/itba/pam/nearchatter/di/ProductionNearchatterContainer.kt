package ar.edu.itba.pam.nearchatter.di

import android.content.Context
import ar.edu.itba.pam.nearchatter.login.LoginPresenter
import ar.edu.itba.pam.nearchatter.login.LoginView
import ar.edu.itba.pam.nearchatter.login.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.login.repository.UserRepository
import ar.edu.itba.pam.nearchatter.services.INearbyService

class ProductionNearchatterContainer(context: Context) : NearchatterContainer {
    private val nearchatterModule: NearchatterModule = NearchatterModule(context)
    private var userRepository: IUserRepository? = null
    private var loginPresenter: LoginPresenter? = null
    private var nearbyService: INearbyService? = null

    override fun getApplicationContext(): Context {
        return nearchatterModule.getApplicationContext()
    }

    override fun getUserRepository(): IUserRepository {
        if (userRepository == null) {
            userRepository = nearchatterModule.provideUserRepository()
        }
        return userRepository as IUserRepository;
    }

    override fun getLoginPresenter(view: LoginView): LoginPresenter {
        if (loginPresenter == null) {
            loginPresenter = nearchatterModule.provideLoginPresenter(
                view,
                getUserRepository() as UserRepository
            )
        }
        return loginPresenter as LoginPresenter;
    }

    override fun getNearbyService(): INearbyService {
        if(nearbyService == null) {
            nearbyService = nearchatterModule.provideNearbyService()
        }
        return nearbyService as INearbyService
    }


}