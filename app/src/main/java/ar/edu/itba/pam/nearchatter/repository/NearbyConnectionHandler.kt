package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.models.Device
import com.google.android.gms.nearby.connection.*


class NearbyConnectionHandler(
    private val hwId: String,
) : INearbyConnectionHandler {
    companion object {
        const val USERNAME_PREFIX = "us"
        const val MESSAGE_PREFIX = "ms"
        const val MAGIC_PREFIX = "nc"
    }

    private val endpointIdDevicesConnecting: MutableSet<String> = HashSet()
    private val endpointIdDevices: MutableMap<String, Device> = HashMap()
    private var connectionsClient: ConnectionsClient? = null
    private var username: String? = null
    private var onConnected: OnConnectCallback? = null
    private var onMessage: OnMessageCallback? = null
    private var onDisconnected: OnDisconnectCallback? = null
    private var initialized: Boolean = false

    override fun init(
        connectionsClient: ConnectionsClient,
        username: String,
        onConnected: OnConnectCallback,
        onMessage: OnMessageCallback,
        onDisconnected: OnDisconnectCallback
    ) {
        this.connectionsClient = connectionsClient
        this.username = username
        this.onConnected = onConnected
        this.onMessage = onMessage
        this.onDisconnected = onDisconnected

        initialized = true
    }

    override fun sendMessage(endpointId: String, message: String) {
        if (!initialized) {
            throw IllegalStateException("Not initialized")
        }

        connectionsClient!!.sendPayload(
            endpointId,
            Payload.fromBytes((MAGIC_PREFIX + MESSAGE_PREFIX + message).toByteArray(Charsets.UTF_8))
        )
    }

    override fun createEndpointDiscoveryCallback(): EndpointDiscoveryCallback {
        if (!initialized) {
            throw IllegalStateException("Not initialized")
        }

        return EndpointDiscovery()
    }

    override fun createConnectionLifecycleCallback(): ConnectionLifecycleCallback {
        if (!initialized) {
            throw IllegalStateException("Not initialized")
        }

        return ConnectionLifecycle()
    }

    // Callbacks for finding other devices
    private inner class EndpointDiscovery : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            if (endpointIdDevices.contains(endpointId) || endpointIdDevicesConnecting.contains(endpointId)) {
                println("On endpoint Found existing device: $endpointId")
                return
            }

            println("On endpoint Found: $endpointId")
            endpointIdDevicesConnecting.plus(endpointId)

            connectionsClient!!
                .requestConnection(username!!, endpointId, ConnectionLifecycle())
                .addOnSuccessListener { println("connected") }
                .addOnFailureListener { e -> println(e) }
        }

        override fun onEndpointLost(endpointId: String) {
            println("On endpoint lost: $endpointId")
            onDisconnected!!.accept(endpointId)
        }
    }

    // Callbacks for connections to other devices
    private inner class ConnectionLifecycle : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            connectionsClient!!.acceptConnection(endpointId, CustomPayloadCallback())
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            println("On connection result: $endpointId, $resolution (${resolution.status})")
            if (resolution.status.isSuccess) {
                println("sending hwid to $endpointId")
                connectionsClient!!.sendPayload(
                    endpointId,
                    Payload.fromBytes(
                        (
                            MAGIC_PREFIX +
                                USERNAME_PREFIX +
                                username!!.length +
                                USERNAME_PREFIX +
                                username +
                                hwId
                            ).toByteArray(Charsets.UTF_8)
                    )
                )
            }
        }

        override fun onDisconnected(endpointId: String) {
            println("On connection disconnected: $endpointId")
            onDisconnected!!.accept(endpointId)
        }
    }

    // Callbacks for receiving payloads
    private inner class CustomPayloadCallback : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            var decoded = String(payload.asBytes()!!, Charsets.UTF_8)

            // Prevent unknown connections
            if (!decoded.startsWith(MAGIC_PREFIX)) {
                println("invalid message from $endpointId: $decoded")
                return
            }

            decoded = decoded.substringAfter(MAGIC_PREFIX)

            println("received from $endpointId: $decoded")
            if (decoded.startsWith(USERNAME_PREFIX)) {
                decoded = decoded.substringAfter(USERNAME_PREFIX)

                val usernamePrefixIndex = decoded.indexOf(USERNAME_PREFIX)

                val usernameLength = decoded.substring(0, usernamePrefixIndex).toInt()
                val username = decoded.substring(usernamePrefixIndex + USERNAME_PREFIX.length, usernamePrefixIndex + USERNAME_PREFIX.length + usernameLength)

                decoded = decoded.substringAfter(username)

                val otherHwId = decoded

                println("received from $endpointId: username = $username, hwid = $otherHwId")
                onConnected!!.accept(endpointId, otherHwId, username)
            } else if (decoded.startsWith(MESSAGE_PREFIX)) {
                val message = decoded.substringAfter(MESSAGE_PREFIX)

                println("received from $endpointId: message = $message")
                val device = endpointIdDevices[endpointId] ?: return

                onMessage!!.accept(device.getId(), message)
            }
        }

        override fun onPayloadTransferUpdate(p0: String, p1: PayloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
            // after the call to onPayloadReceived().
        }
    }
}
