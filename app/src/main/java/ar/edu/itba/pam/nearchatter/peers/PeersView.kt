package ar.edu.itba.pam.nearchatter.peers

import ar.edu.itba.pam.nearchatter.domain.Conversation

interface PeersView {
    fun bind(conversations: List<Conversation>)

    fun setOnline(userId: String, connected: Boolean)
}
