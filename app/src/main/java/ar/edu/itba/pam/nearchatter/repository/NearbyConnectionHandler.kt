package ar.edu.itba.pam.nearchatter.repository

import android.util.Log
import com.google.android.gms.nearby.connection.*


class NearbyConnectionHandler(
    private val hwId: String,
) : INearbyConnectionHandler {
    companion object {
        const val USERNAME_PREFIX = "us"
        const val MESSAGE_PREFIX = "ms"
        const val MAGIC_PREFIX = "nc"
    }

    private val tag = "NearbyConnectionHandler"
    private val endpointIdDevicesConnecting: MutableSet<String> = HashSet()
    private val endpointIdDevicesHwId: MutableMap<String, String> = HashMap()
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
            if (endpointIdDevicesHwId.contains(endpointId) || endpointIdDevicesConnecting.contains(endpointId)) {
                Log.i(tag, "On endpoint Found existing device: $endpointId")
                return
            }

            Log.i(tag, "On endpoint Found: $endpointId")
            endpointIdDevicesConnecting.plus(endpointId)

            connectionsClient!!
                .requestConnection(username!!, endpointId, ConnectionLifecycle())
                .addOnSuccessListener { Log.i(tag, "initial connection, passing hw ids...") }
                .addOnFailureListener { e ->
                    Log.e(tag, e.toString(), e)
                    endpointIdDevicesConnecting.remove(endpointId)
                    endpointIdDevicesHwId.remove(endpointId)
                    connectionsClient!!.disconnectFromEndpoint(endpointId)
                }
        }

        override fun onEndpointLost(endpointId: String) {
            Log.i(tag, "On endpoint lost: $endpointId - Do Nothing")
            endpointIdDevicesConnecting.remove(endpointId)
        }
    }

    // Callbacks for connections to other devices
    private inner class ConnectionLifecycle : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            connectionsClient!!.acceptConnection(endpointId, CustomPayloadCallback())
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            Log.i(tag, "On connection result: $endpointId, $resolution (${resolution.status})")
            if (resolution.status.isSuccess) {
                Log.i(tag, "sending hwid to $endpointId")
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
            Log.i(tag, "On connection disconnected: $endpointId")
            endpointIdDevicesConnecting.remove(endpointId)
            endpointIdDevicesHwId.remove(endpointId)
            connectionsClient!!.disconnectFromEndpoint(endpointId)
            onDisconnected!!.accept(endpointId)
        }
    }

    // Callbacks for receiving payloads
    private inner class CustomPayloadCallback : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            var decoded = String(payload.asBytes()!!, Charsets.UTF_8)

            // Prevent unknown connections
            if (!decoded.startsWith(MAGIC_PREFIX)) {
                Log.i(tag, "invalid message from $endpointId: $decoded")
                return
            }

            decoded = decoded.substringAfter(MAGIC_PREFIX)

            Log.i(tag, "received from $endpointId: $decoded")
            if (decoded.startsWith(USERNAME_PREFIX)) {
                decoded = decoded.substringAfter(USERNAME_PREFIX)

                val usernamePrefixIndex = decoded.indexOf(USERNAME_PREFIX)

                val usernameLength = decoded.substring(0, usernamePrefixIndex).toInt()
                val username = decoded.substring(usernamePrefixIndex + USERNAME_PREFIX.length, usernamePrefixIndex + USERNAME_PREFIX.length + usernameLength)

                decoded = decoded.substringAfter(username)

                val otherHwId = decoded

                Log.i(tag, "received from $endpointId: username = $username, hwid = $otherHwId")
                endpointIdDevicesConnecting.remove(endpointId)
                endpointIdDevicesHwId[endpointId] = otherHwId

                onConnected!!.accept(endpointId, otherHwId, username)
            } else if (decoded.startsWith(MESSAGE_PREFIX)) {
                val message = decoded.substringAfter(MESSAGE_PREFIX)

                Log.i(tag, "received from $endpointId: message = $message")
                val otherHwId = endpointIdDevicesHwId[endpointId] ?: return

                onMessage!!.accept(otherHwId, message)
            }
        }

        override fun onPayloadTransferUpdate(p0: String, p1: PayloadTransferUpdate) {
            // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
            // after the call to onPayloadReceived().
        }
    }
}
