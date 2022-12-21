package ar.edu.itba.pam.nearchatter.db.sharedPreferences

interface ISharedPreferencesStorage {
    fun getUsername(): String?

    fun setUsername(username: String)
}
