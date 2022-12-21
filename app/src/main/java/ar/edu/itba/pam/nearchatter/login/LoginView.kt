package ar.edu.itba.pam.nearchatter.login

interface LoginView {
    fun bind();

    fun setCanLogIn(canLogIn: Boolean)

    fun setUsername(username: String)
}
