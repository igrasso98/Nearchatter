package ar.edu.itba.pam.nearchatter.domain

import java.time.LocalDate

class Message(
    private var senderId: String,
    private var receiverId: String,
    private var payload: String,
    private var sendAt: LocalDate
) {
    fun getReceiverId(): String {
        return this.receiverId
    }

    fun getSenderId(): String {
        return this.senderId
    }

    fun getPayload(): String {
        return this.payload
    }

    fun getSendAt(): LocalDate {
        return this.sendAt
    }
}
