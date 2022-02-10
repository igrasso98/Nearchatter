package ar.edu.itba.pam.nearchatter.services

import android.content.Context
import android.provider.Settings
import ar.edu.itba.pam.nearchatter.models.Device
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.gms.nearby.connection.ConnectionsClient

import com.google.android.gms.nearby.connection.DiscoveryOptions
import java.lang.IllegalStateException


class NearbyService(val context: Context) : INearbyService {
    companion object {
        const val SERVICE_ID = "ar.edu.itba.pam.nearchatter"
        const val INITIALIZATION_PREFIX = "id"
        const val USERNAME_PREFIX = "id"
        const val MESSAGE_PREFIX = "ms"
    }

    private val connectionsClient: ConnectionsClient = Nearby.getConnectionsClient(context)
    // TODO: Move to repo
    private val endpointIdDevices: MutableMap<String, Device> = HashMap()
    // TODO: Move to repo
    private val hwIdDevices: MutableMap<String, Device> = HashMap()
    private var acceptsConnections = false
    private var stopping = false

    override fun openConnections(username: String, onNewDevice: NewDeviceCallback) {
        if (stopping) {
            throw ConcurrentModificationException()
        }
        acceptsConnections = true

        val helper = ConnectionHelper(
            username,
            { endpointId, id, username ->
                println("Connected with: $endpointId -> $id (username: $username)")
                val device = Device(id, endpointId, username)
                hwIdDevices[id] = device
                endpointIdDevices[endpointId] = device
            },
            { endpointId, message -> println("Message: $message from $endpointId") },
            { endpointId ->
                println("Disconnected: $endpointId")
                val device = endpointIdDevices[endpointId]
                if (device != null) {
                    hwIdDevices.remove(device.getId())
                    endpointIdDevices.remove(endpointId)
                }
            }
        )
        startAdvertising(username, helper.ConnectionLifecycle())
        startDiscovery(helper.EndpointDiscovery())
    }

    override fun sendMessage(id: String, message: String) {
        if (!acceptsConnections) {
            throw IllegalStateException()
        }

        val device = hwIdDevices[id] ?: return
        connectionsClient.sendPayload(
            device.getEndpointId(),
            Payload.fromBytes("$MESSAGE_PREFIX$message".toByteArray(Charsets.UTF_8))
        )
    }

    override fun closeConnections() {
        acceptsConnections = false
        stopping = true

        stopDiscovery()
        stopAdvertising()
        endpointIdDevices.clear()
        hwIdDevices.clear()

        stopping = false
    }

    private fun startAdvertising(
        username: String,
        lifecycle: ConnectionLifecycleCallback
    ) {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        connectionsClient
            .startAdvertising(username, SERVICE_ID, lifecycle, advertisingOptions)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    private fun startDiscovery(
        discovery: EndpointDiscoveryCallback
    ) {
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build()
        connectionsClient
            .startDiscovery(SERVICE_ID, discovery, discoveryOptions)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    private fun stopAdvertising() {
        connectionsClient
            .stopAdvertising()
    }

    private fun stopDiscovery() {
        connectionsClient
            .stopDiscovery()
    }

    inner class ConnectionHelper(
        private val username: String,
        private val onConnected: OnConnectCallback,
        private val onMessage: OnMessageCallback,
        private val onDisconnected: OnDisconnectCallback,
    ) {
        inner class EndpointDiscovery : EndpointDiscoveryCallback()
        {
            override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
                println("On endpoint Found: $endpointId")
                connectionsClient
                    .requestConnection(username, endpointId, ConnectionLifecycle())
                    .addOnSuccessListener { println("connected") }
                    .addOnFailureListener { e -> println(e) }
            }

            override fun onEndpointLost(endpointId: String) {
                println("On endpoint lost: $endpointId")
            }
        }

        inner class ConnectionLifecycle : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
                connectionsClient
                    .acceptConnection(endpointId, CustomPayloadCallback())
            }

            override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
                println("On connection result: $endpointId, $resolution (${resolution.status})")
                if (resolution.status.isSuccess) {
                    println("sending hwid to $endpointId")
                    val hwId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

                    connectionsClient.sendPayload(
                        endpointId,
                        Payload.fromBytes("$INITIALIZATION_PREFIX${hwId.toByteArray(Charsets.UTF_8)}".toByteArray())
                    )
                }
            }

            override fun onDisconnected(endpointId: String) {
                println("On connection disconnected: $endpointId")
                onDisconnected.accept(endpointId)
            }
        }

        inner class CustomPayloadCallback : PayloadCallback()
        {
            override fun onPayloadReceived(endpointId: String, payload: Payload) {
                val decoded = String(payload.asBytes()!!, Charsets.UTF_8)
                println("received from $endpointId: $decoded")

                if (decoded.startsWith(INITIALIZATION_PREFIX)) {
                    val data = decoded.substringAfter(INITIALIZATION_PREFIX)

                    val usernameLength = decoded.substringBefore(USERNAME_PREFIX).toInt()
                    val username = decoded.substring(usernameLength)
                    val hwid = decoded.substringAfter(INITIALIZATION_PREFIX)
                    onConnected.accept(endpointId, hwid, username)
                } else {
                    val message = decoded.substringAfter(MESSAGE_PREFIX)
                    onMessage.accept(endpointId, message)
                }
            }

            override fun onPayloadTransferUpdate(p0: String, p1: PayloadTransferUpdate) {}
        }
    }
}

fun interface OnConnectCallback {
    fun accept(endpointId: String, id: String, username: String)
}


fun interface OnMessageCallback {
    fun accept(endpointId: String, message: String)
}

fun interface OnDisconnectCallback {
    fun accept(endpointId: String)
}
