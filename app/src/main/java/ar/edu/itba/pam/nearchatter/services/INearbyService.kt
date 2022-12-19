package ar.edu.itba.pam.nearchatter.services

import ar.edu.itba.pam.nearchatter.domain.User
import java.util.function.Consumer

interface INearbyService {
    /**
     * Broadcasts and searches for devices with Nearchatter app open
     * For each one of them, it tries to connect to them.
     * @throws ConcurrentModificationException if the service is stopping
     */
    fun openConnections(username: String)

    fun sendMessage(message: String, receiverId: String)

    fun closeConnections()

    fun setOnConnectCallback(callback: Consumer<User>?)

    fun setOnDisconnectCallback(callback: Consumer<User>?)
}
