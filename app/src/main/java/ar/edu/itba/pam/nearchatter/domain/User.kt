package ar.edu.itba.pam.nearchatter.domain

class User(
    private val id: String,
    private val username: String
) {
    fun getUserId(): String {
        return id
    }

    fun getUsername(): String {
        return username
    }
}
