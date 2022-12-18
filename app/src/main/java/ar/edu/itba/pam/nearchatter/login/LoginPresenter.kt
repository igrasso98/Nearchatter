package ar.edu.itba.pam.nearchatter.login

import android.annotation.SuppressLint
import android.provider.Settings
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import java.lang.Exception

class LoginPresenter(
    view: LoginView,
    private val userRepository: IUserRepository,
    private val sharedPreferencesStorage: ISharedPreferencesStorage,
    private val schedulerProvider: SchedulerProvider,
    private val hwid: String,
) {

    @SuppressLint("CheckResult")
    fun onUsernameConfirm(username: String) {
        if (sharedPreferencesStorage.isActive()) {
            userRepository.addUser(User(hwid, username, true))
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onUserAdded, this::onUserAddedFailed)
        } else {
            userRepository.updateUsername(hwid, username)
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onUserAddedFailed)
        }
    }

    private fun onUserAdded(unit: Unit) {
        sharedPreferencesStorage.deactivate()
        println(unit)
    }

    private fun onSuccess(unit: Unit) {
        println(unit)
    }

    private fun onUserAddedFailed(t: Throwable) {
        println(t.toString())
    }
}
