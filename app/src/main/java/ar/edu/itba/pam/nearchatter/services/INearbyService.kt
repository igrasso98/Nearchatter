package ar.edu.itba.pam.nearchatter.services

import ar.edu.itba.pam.nearchatter.models.Device

interface INearbyService {
    /**
     * Broadcasts and searches for devices with Nearchatter app open
     * For each one of them, it tries to connect to them.
     * Whenever a Device is connected or disconnected, it calls the consumer
     * @throws ConcurrentModificationException if the service is stopping
     */
    fun openConnections(username: String, onNewDevice: NewDeviceCallback)

    fun sendMessage(id: String, message: String)

    fun closeConnections()
}

fun interface NewDeviceCallback {
    fun accept(device: Device)
}
