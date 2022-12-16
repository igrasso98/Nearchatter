package ar.edu.itba.pam.nearchatter.db.room.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.ForeignKey.RESTRICT
import androidx.room.PrimaryKey
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
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
class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sender_id") val senderId: String,
    @ColumnInfo(name = "receiver_id") val receiverId: String,
    @ColumnInfo(name = "payload") val payload: String,
    @ColumnInfo(name = "send_at") val sendAt: LocalDate,
)
