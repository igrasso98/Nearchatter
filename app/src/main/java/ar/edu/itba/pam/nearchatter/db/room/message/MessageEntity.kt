package ar.edu.itba.pam.nearchatter.db.room.message

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
import ar.edu.itba.pam.nearchatter.db.room.utils.LocalDateTimeTypeConverter
import java.time.LocalDateTime

@Entity(
    tableName = "messages", foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["sender_id"],
            onDelete = RESTRICT,
            onUpdate = CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["receiver_id"],
            onDelete = RESTRICT,
            onUpdate = CASCADE
        )
    ],
    indices = [
        Index("sender_id"),
        Index("receiver_id"),
    ]
)
@TypeConverters(LocalDateTimeTypeConverter::class)
class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "message_id") val id: String,
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "receiver_id") val receiverId: String,
    @ColumnInfo(name = "payload") val payload: String,
    @ColumnInfo(name = "send_at") val sendAt: LocalDateTime,
)
