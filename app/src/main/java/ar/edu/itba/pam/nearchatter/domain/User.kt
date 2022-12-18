package ar.edu.itba.pam.nearchatter.domain

class User(
    private val id: String,
    private val username: String,
    private val isConnected: Boolean
) {
    fun getUserId(): String {
        return id
    }

    fun getUsername(): String {
        return username
    }

    fun isConnected(): Boolean {
        return isConnected
    }
}
