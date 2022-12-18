package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.models.Device
import java.util.function.Consumer

interface INearbyRepository {
    fun openConnections(username: String)

    fun sendMessage(message: Message)

    fun closeConnections()

    fun setOnConnectCallback(callback: Consumer<Device>?)

    fun setOnDisconnectCallback(callback: Consumer<Device>?)

    fun setOnMessageCallback(callback: Consumer<Message>?)
}
