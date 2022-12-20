package ar.edu.itba.pam.nearchatter.domain

import java.time.LocalDateTime

class Message(
    private var id: String,
    private var senderId: String,
    private var receiverId: String,
    private var payload: String,
    private var sendAt: LocalDateTime
) {
    fun getId(): String {
        return this.id
    }

    fun getReceiverId(): String {
        return this.receiverId
    }

    fun getSenderId(): String {
        return this.senderId
    }

    fun getPayload(): String {
        return this.payload
    }

    fun getSendAt(): LocalDateTime {
        return this.sendAt
    }
}
