package ar.edu.itba.pam.nearchatter.login.ui

import ar.edu.itba.pam.nearchatter.login.OnUsernameConfirmListener

interface LoginFormView {
    fun bind()

    fun setUsername(username: String)

    fun setOnUsernameConfirmListener(listener: OnUsernameConfirmListener)
}
