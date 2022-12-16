package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Message

interface INearbyRepository {
    fun openConnections(username: String)

    fun sendMessage(message: Message)

    fun closeConnections()
}
