package ar.edu.itba.pam.nearchatter.db.room.user

import androidx.room.*
import ar.edu.itba.pam.nearchatter.db.room.message.MessageEntity

@Entity(
    tableName = "users",
    foreignKeys = [
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["message_id"],
            childColumns = ["last_message_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("last_message_id")
    ]
)
class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "last_message_id")
    val lastMessageId: Long?
)
