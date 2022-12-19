package ar.edu.itba.pam.nearchatter.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ar.edu.itba.pam.nearchatter.db.room.message.MessageDao
import ar.edu.itba.pam.nearchatter.db.room.message.MessageEntity
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity

@Database(
    entities = [UserEntity::class, MessageEntity::class],
    version = 7
)
abstract class NearchatterDb : RoomDatabase() {
    companion object {
        fun getInstance(context: Context): NearchatterDb? {
            if (instance == null) {
                synchronized(NearchatterDb::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NearchatterDb::class.java,
                        NAME
                    ).fallbackToDestructiveMigration().build()
                }

            }
            return instance
        }

        const val NAME = "pam_db"

        @Volatile
        private var instance: NearchatterDb? = null
    }

    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao

}
