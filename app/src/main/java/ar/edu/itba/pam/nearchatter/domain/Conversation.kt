package ar.edu.itba.pam.nearchatter.domain

import java.time.LocalDate

class Conversation(user: User, lastMessage: Message?) {
    private var user: User = user
    private var lastMessage: Message? = lastMessage

    fun getUsername(): String {
        return this.user.getUsername()
    }

    fun getLastMessagePayload(): String? {
        if (lastMessage != null) {
            return this.lastMessage?.getPayload()
        }
        return null
    }

    fun getLastMessageSendAt(): LocalDate? {
        if (lastMessage != null) {
            return this.lastMessage?.getSendAt()
        }
        return null
    }
}
