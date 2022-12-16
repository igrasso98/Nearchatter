package ar.edu.itba.pam.nearchatter.services

import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.models.Device
import java.util.function.Consumer

interface INearbyService {
    /**
     * Broadcasts and searches for devices with Nearchatter app open
     * For each one of them, it tries to connect to them.
     * @throws ConcurrentModificationException if the service is stopping
     */
    fun openConnections(username: String)

    fun sendMessage(message: Message)

    fun closeConnections()

    fun setOnConnectCallback(callback: Consumer<Device>)

    fun setOnDisconnectCallback(callback: Consumer<Device>)

    fun setOnMessageCallback(callback: Consumer<Message>)
}
