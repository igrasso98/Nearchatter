package ar.edu.itba.pam.nearchatter.db.room.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var userId: Long? = null

    @ColumnInfo(name = "username")
    var username: String? = null
}