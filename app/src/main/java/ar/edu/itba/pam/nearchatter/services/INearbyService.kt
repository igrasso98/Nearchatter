package ar.edu.itba.pam.nearchatter.services

import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.models.Device

interface INearbyService {
    /**
     * Broadcasts and searches for devices with Nearchatter app open
     * For each one of them, it tries to connect to them.
     * @throws ConcurrentModificationException if the service is stopping
     */
    fun openConnections(username: String)

    fun sendMessage(message: Message)

    fun closeConnections()

//    fun setOnConnectCallback(callback: NewDeviceCallback)
//
//    fun setOnMessageCallback(callback: MessageCallback)
}

fun interface NewDeviceCallback {
    fun accept(device: Device)
}

fun interface MessageCallback {
    fun accept(message: Message)
}
