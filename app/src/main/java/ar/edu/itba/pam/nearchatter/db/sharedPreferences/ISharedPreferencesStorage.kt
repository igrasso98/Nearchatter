package ar.edu.itba.pam.nearchatter.db.sharedPreferences

interface ISharedPreferencesStorage {
    fun isActive(): Boolean

    fun deactivate()

    fun getUserId(): Long

    fun deleteUserId()

    fun setUserId(userId: Long)
}