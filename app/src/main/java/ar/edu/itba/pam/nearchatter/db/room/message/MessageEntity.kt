package ar.edu.itba.pam.nearchatter.db.room.message

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
import ar.edu.itba.pam.nearchatter.db.room.utils.LocalDateTypeConverter
import java.time.LocalDate

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
    ]
)
@TypeConverters(LocalDateTypeConverter::class)
class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id") val id: Long,
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "receiver_id") val receiverId: String,
    @ColumnInfo(name = "payload") val payload: String,
    @ColumnInfo(name = "send_at") val sendAt: LocalDate,
)
