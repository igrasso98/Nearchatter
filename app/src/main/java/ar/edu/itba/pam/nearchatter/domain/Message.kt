package ar.edu.itba.pam.nearchatter.domain

class Message(
    senderId: Long,
    receiverId: Long,
    payload: String,
    sendAt: String,
    read: Boolean
) {
    private var senderId: Long = senderId
    private var receiverId: Long = receiverId
    private var payload: String = payload
    private var sendAt: String = sendAt
    private var read: Boolean = read

    fun getPayload(): String {
        return this.payload
    }

    fun getSendAt(): String {
        return this.sendAt
    }

    fun getisRead(): Boolean {
        return read
    }

}