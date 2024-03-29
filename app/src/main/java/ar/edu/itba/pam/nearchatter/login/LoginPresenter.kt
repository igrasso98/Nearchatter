package ar.edu.itba.pam.nearchatter.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.VisibleForTesting
import ar.edu.itba.pam.nearchatter.db.sharedPreferences.ISharedPreferencesStorage
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import java.lang.ref.WeakReference

class LoginPresenter(
    view: LoginView,
    private val userRepository: IUserRepository,
    private val sharedPreferencesStorage: ISharedPreferencesStorage,
    private val schedulerProvider: SchedulerProvider,
    private val hwid: String,
) {
    private val tag = "LoginPresenter"
    private val permissions: MutableSet<Int> = mutableSetOf()
    private val allPermissions: Set<Int> = setOf(1, 2, 3, 4, 5, 6)
    private var view: WeakReference<LoginView> = WeakReference(view)

    @SuppressLint("CheckResult")
    fun onUsernameConfirm(username: String): String {
        val cleanUsername = username.trim()
        schedulerProvider.io().scheduleDirect {
            sharedPreferencesStorage.setUsername(cleanUsername)
            userRepository.addUser(User(hwid, cleanUsername))
                .subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
                .subscribe({ onUserAdded(it) }, { onUserAddedFailed(it) })
        }
        return cleanUsername
    }

    fun onViewAttached(loginView: LoginView) {
        view = WeakReference<LoginView>(loginView)
        retrieveUsername()
    }

    fun onViewDetached() {
        view = WeakReference<LoginView>(null)
    }

    fun onPermissionGranted(requestedCode: Int) {
        permissions.add(requestedCode)
        checkPermissions()
    }

    private fun checkPermissions() {
        if (permissions.containsAll(allPermissions)) {
            view.get()?.setCanLogIn(true)
        } else {
            view.get()?.setCanLogIn(false)
        }
    }

    @VisibleForTesting
    fun retrieveUsername() {
        view.get()?.setUsername(sharedPreferencesStorage.getUsername() ?: "")
    }

    private fun onUserAdded(unit: Unit) {
        Log.i(tag, "onUserAdded")
    }

    private fun onUserAddedFailed(t: Throwable) {
        Log.e(tag, "onUserAddedFailed", t)
    }
}
