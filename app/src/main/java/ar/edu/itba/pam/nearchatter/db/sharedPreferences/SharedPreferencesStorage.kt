package ar.edu.itba.pam.nearchatter.db.sharedPreferences

import android.content.SharedPreferences

class SharedPreferencesStorage(private val preferences: SharedPreferences) :
    ISharedPreferencesStorage {
    private val FTU_KEY: String = "is-ftu"

    override fun isActive(): Boolean {
        return preferences.getBoolean(FTU_KEY, true);
    }

    override fun deactivate() {
        preferences.edit().putBoolean(FTU_KEY, true).apply()
    }
}