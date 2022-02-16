package ar.edu.itba.pam.nearchatter.services

import android.content.Context
import android.provider.Settings
import ar.edu.itba.pam.nearchatter.models.Device
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*


class NearbyService(val context: Context) : INearbyService {
    companion object {
        const val SERVICE_ID = "ar.edu.itba.pam.nearchatter"
        const val INITIALIZATION_PREFIX = "id"
        const val MESSAGE_PREFIX = "ms"
        const val MAGIC_PREFIX = "nc"
        val CHAR_REGEX = "[^0-9]".toRegex()
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
            Payload.fromBytes((MAGIC_PREFIX + MESSAGE_PREFIX + message).toByteArray(Charsets.UTF_8))
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
        // TODO: CHange to SHA256
        private val hwId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

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
                onDisconnected.accept(endpointId)
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
                    connectionsClient.sendPayload(
                        endpointId,
                        Payload.fromBytes((
                             MAGIC_PREFIX +
                             INITIALIZATION_PREFIX +
                             username.length +
                             username +
                             hwId
                        ).toByteArray(Charsets.UTF_8))
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
                var decoded = String(payload.asBytes()!!, Charsets.UTF_8)

                // Prevent unknown connections
                if (!decoded.startsWith(MAGIC_PREFIX)) {
                    println("invalid message from $endpointId: $decoded")
                    return
                }

                decoded = decoded.substringAfter(MAGIC_PREFIX)

                println("received from $endpointId: $decoded")
                if (decoded.startsWith(INITIALIZATION_PREFIX)) {
                    decoded = decoded.substringAfter(INITIALIZATION_PREFIX)

                    val usernameIndex = CHAR_REGEX.find(decoded)!!.range.first
                    val usernameLength = decoded.substring(0, usernameIndex).toInt()
                    val username = decoded.substring(usernameIndex, usernameLength)

                    decoded = decoded.substringAfter(username)

                    val otherHwId = decoded

                    println("received from $endpointId: username = $username, hwid = $otherHwId")
                    onConnected.accept(endpointId, otherHwId, username)
                } else if (decoded.startsWith(MESSAGE_PREFIX)) {
                    val message = decoded.substringAfter(MESSAGE_PREFIX)

                    println("received from $endpointId: message = $message")
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

fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }
