package ar.edu.itba.pam.nearchatter.domain

class Message(
    senderId: String,
    receiverId: String,
    payload: String,
    sendAt: String,
    read: Boolean
) {
}