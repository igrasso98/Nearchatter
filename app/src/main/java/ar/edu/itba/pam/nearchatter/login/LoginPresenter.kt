package ar.edu.itba.pam.nearchatter.login

import android.annotation.SuppressLint
import android.util.Log
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider

class LoginPresenter(
    view: LoginView,
    private val userRepository: IUserRepository,
    private val sharedPreferencesStorage: ISharedPreferencesStorage,
    private val schedulerProvider: SchedulerProvider,
    private val hwid: String,
) {
    private val tag = "LoginPresenter"

    @SuppressLint("CheckResult")
    fun onUsernameConfirm(username: String) {
        if (sharedPreferencesStorage.isActive()) {
            schedulerProvider.io().scheduleDirect {
                userRepository.addUser(User(hwid, username, true))
                    .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                    .subscribe({ onUserAdded(it) }, { onUserAddedFailed(it) })
            }
        } else {
            schedulerProvider.io().scheduleDirect {
                userRepository.updateUsername(hwid, username)
                    .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                    .subscribe({ onSuccess(it) }, { onUserAddedFailed(it) })
            }
        }
    }

    private fun onUserAdded(unit: Unit) {
        sharedPreferencesStorage.deactivate()
        Log.i(tag, unit.toString())
    }

    private fun onSuccess(unit: Unit) {
        Log.i(tag, unit.toString())
    }

    private fun onUserAddedFailed(t: Throwable) {
        Log.i(tag, t.toString())
    }
}
