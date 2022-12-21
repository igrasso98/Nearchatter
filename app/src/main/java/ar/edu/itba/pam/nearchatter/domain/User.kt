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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}
