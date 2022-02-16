package ar.edu.itba.pam.nearchatter.db.sharedPreferences

import android.content.SharedPreferences

class SharedPreferencesStorage(private val preferences: SharedPreferences) :
    ISharedPreferencesStorage {
    private val FTU_KEY: String = "is-ftu"
    private val USER_ID_KEY: String = "user-id"

    override fun isActive(): Boolean {
        return preferences.getBoolean(FTU_KEY, true);
    }

    override fun deactivate() {
        preferences.edit().putBoolean(FTU_KEY, false).apply()
    }


    override fun getUserId(): Long {
        return preferences.getLong(USER_ID_KEY, -1L)
    }

    override fun deleteUserId() {
        preferences.edit().putLong(USER_ID_KEY, -1L).apply()
    }

    override fun setUserId(userId: Long) {
        return preferences.edit().putLong(USER_ID_KEY, userId).apply()
    }
}