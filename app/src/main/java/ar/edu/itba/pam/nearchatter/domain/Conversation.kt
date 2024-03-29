package ar.edu.itba.pam.nearchatter.domain

import java.time.LocalDateTime

class Conversation(user: User, lastMessage: Message?) {
    private var user: User = user
    private var lastMessage: Message? = lastMessage

    fun getUserId(): String {
        return user.getUserId()
    }

    fun getUsername(): String {
        return this.user.getUsername()
    }

    fun getLastMessagePayload(): String? {
        if (lastMessage != null) {
            return this.lastMessage?.getPayload()
        }
        return null
    }

    fun getLastMessageSendAt(): LocalDateTime? {
        if (lastMessage != null) {
            return this.lastMessage?.getSendAt()
        }
        return null
    }
}
