package ar.edu.itba.pam.nearchatter.chat

import ar.edu.itba.pam.nearchatter.domain.Message

interface ChatView {
    fun bind(conversations: List<Message>)
}