package ar.edu.itba.pam.nearchatter.repository

import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback

fun interface OnConnectCallback {
    fun accept(endpointId: String, hwId: String, username: String)
}

fun interface OnMessageCallback {
    fun accept(hwId: String, message: String)
}

fun interface OnDisconnectCallback {
    fun accept(hwId: String)
}

interface INearbyConnectionHandler {
    fun init(
        connectionsClient: ConnectionsClient,
        username: String,
        onConnected: OnConnectCallback,
        onMessage: OnMessageCallback,
        onDisconnected: OnDisconnectCallback
    )

    fun closeConnections()

    fun sendMessage(endpointId: String, message: String)

    fun createEndpointDiscoveryCallback() : EndpointDiscoveryCallback

    fun createConnectionLifecycleCallback(): ConnectionLifecycleCallback
}
