package ar.edu.itba.pam.nearchatter.db.sharedPreferences

import android.content.SharedPreferences

class SharedPreferencesStorage(private val preferences: SharedPreferences) :
    ISharedPreferencesStorage {
    private val SESSION_KEY: String = "is-active"

    override fun isActive(): Boolean {
        return preferences.getBoolean(SESSION_KEY, false);
    }

    override fun deactivate() {
        preferences.edit().putBoolean(SESSION_KEY, false).apply()
    }

    override fun activate() {
        preferences.edit().putBoolean(SESSION_KEY, true).apply()
    }
}