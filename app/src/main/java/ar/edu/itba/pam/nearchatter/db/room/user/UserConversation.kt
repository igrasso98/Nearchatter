package ar.edu.itba.pam.nearchatter.db.room.user

import androidx.room.Embedded
import ar.edu.itba.pam.nearchatter.db.room.message.MessageEntity

class UserConversation {
    @Embedded
    var user: UserEntity? = null

    @Embedded
    var lastMessage: MessageEntity? = null
}
