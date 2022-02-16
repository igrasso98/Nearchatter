package ar.edu.itba.pam.nearchatter.db.room.conversation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ar.edu.itba.pam.nearchatter.db.room.message.MessageEntity
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity

@Entity(
    tableName = "conversations", foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["other_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["message_id"],
            childColumns = ["last_message_id"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class ConversationEntity(
    @PrimaryKey
    @ColumnInfo(name = "conversation_id") val conversationId: Long,
    @ColumnInfo(name = "other_id") val otherId: String,
    @ColumnInfo(name = "last_message_id") val lastMessageId: Long?
)