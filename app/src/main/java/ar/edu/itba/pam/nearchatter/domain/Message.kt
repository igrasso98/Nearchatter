package ar.edu.itba.pam.nearchatter.domain

class Message(
    senderId: String,
    receiverId: String,
    payload: String,
    sendAt: String,
    read: Boolean
) {
    private var senderId: String = senderId
    private var receiverId: String = receiverId
    private var payload: String = payload
    private var sendAt: String = sendAt
    private var read: Boolean = read

    fun getReceiverId(): String {
        return this.receiverId
    }

    fun getSenderId(): String {
        return this.senderId
    }

    fun getPayload(): String {
        return this.payload
    }

    fun getSendAt(): String {
        return this.sendAt
    }

    fun getIsRead(): Boolean {
        return read
    }

}