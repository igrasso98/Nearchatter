package ar.edu.itba.pam.nearchatter.login.ui

import ar.edu.itba.pam.nearchatter.login.OnUsernameConfirmListener

interface LoginFormView {
    fun bind()

    fun setOnUsernameConfirmListener(listener: OnUsernameConfirmListener)
}