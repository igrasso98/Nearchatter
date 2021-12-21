package ar.edu.itba.pam.nearchatter.login

import ar.edu.itba.pam.nearchatter.login.domain.User
import ar.edu.itba.pam.nearchatter.login.repository.UserRepository
import java.lang.ref.WeakReference

class LoginPresenter(view: LoginView, private val userRepository: UserRepository) {
    private val view: WeakReference<LoginView> = WeakReference<LoginView>(view)

    fun onUsernameConfirm(username: String){
        userRepository.addUser(User(username))
    }

}