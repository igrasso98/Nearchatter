package ar.edu.itba.pam.nearchatter.db.sharedPreferences

interface ISharedPreferencesStorage {
    fun isActive(): Boolean

    fun deactivate()

    fun activate()
}