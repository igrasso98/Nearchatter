package ar.edu.itba.pam.nearchatter.chat

interface OnMessageSentListener {
    fun onMessageSent(payload: String);

}