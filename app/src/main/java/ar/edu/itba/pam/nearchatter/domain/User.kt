package ar.edu.itba.pam.nearchatter.domain

class User(id: String, username: String) {
    private var user_id: String = id
    private var user_username: String = username

    fun getUserId(): String {
        return user_id
    }

    fun getUsername(): String {
        return user_username
    }
}
