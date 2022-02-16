package ar.edu.itba.pam.nearchatter.domain

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

    fun getLastMessageSendAt(): String? {
        if (lastMessage != null) {
            return this.lastMessage?.getSendAt()
        }
        return null
    }

    fun getLastMessageIsRead(): Boolean? {
        if (lastMessage != null) {
            return this.lastMessage?.getisRead()
        }
        return null
    }

}