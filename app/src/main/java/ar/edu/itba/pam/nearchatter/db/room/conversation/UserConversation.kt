package ar.edu.itba.pam.nearchatter.db.room.conversation

import android.os.Message
import androidx.room.Embedded
import ar.edu.itba.pam.nearchatter.db.room.message.MessageEntity
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity

class UserConversation {
    @Embedded
    var user: UserEntity? = null

    @Embedded
    var lastMessage: MessageEntity? = null
}