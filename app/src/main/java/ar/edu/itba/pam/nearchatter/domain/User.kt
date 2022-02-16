package ar.edu.itba.pam.nearchatter.domain

class User(id: Long, username: String) {
    private var user_id: Long = id
    private var user_username: String = username

    fun getUserId(): Long {
        return user_id
    }

    fun getUsername(): String {
        return user_username
    }
}
