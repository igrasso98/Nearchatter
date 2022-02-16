package ar.edu.itba.pam.nearchatter.login

import android.annotation.SuppressLint
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import java.lang.Exception

class LoginPresenter(
    view: LoginView,
    private val userRepository: IUserRepository,
    private val sharedPreferencesStorage: ISharedPreferencesStorage,
    private val schedulerProvider: SchedulerProvider
) {

    @SuppressLint("CheckResult")
    fun onUsernameConfirm(username: String) {
        var userId: Long = sharedPreferencesStorage.getUserId()
        if (userId == -1L) {
            userRepository.addUser(User(userId, username))
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onUserAdded, this::onUserAddedFailed)
        } else {
            userRepository.updateUsername(userId, username)
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe(this::onSuccess, this::onUserAddedFailed)
        }
    }

    private fun onUserAdded(userId: Long) {
        sharedPreferencesStorage.setUserId(userId)
    }

    private fun onSuccess(unit: Unit) {
        println(unit)
    }

    private fun onUserAddedFailed(t: Throwable) {
    }
}