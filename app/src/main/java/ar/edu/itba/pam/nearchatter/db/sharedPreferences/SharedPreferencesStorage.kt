package ar.edu.itba.pam.nearchatter.db.sharedPreferences

import android.content.SharedPreferences

class SharedPreferencesStorage(private val preferences: SharedPreferences) :
    ISharedPreferencesStorage {
    private val USERNAME_KEY: String = "username"

    override fun getUsername(): String? {
        return preferences.getString(USERNAME_KEY, null)
    }

    override fun setUsername(username: String) {
        preferences.edit().putString(USERNAME_KEY, username).apply()
    }
}
