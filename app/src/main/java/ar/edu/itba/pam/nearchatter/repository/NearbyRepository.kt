package ar.edu.itba.pam.nearchatter.repository

import android.util.Log
import androidx.annotation.VisibleForTesting
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.models.Device
import com.google.android.gms.nearby.connection.*
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer

class NearbyRepository(
    private val hwId: String,
    private val nearbyConnectionHandler: INearbyConnectionHandler,
    private val connectionsClient: ConnectionsClient,
) :
    INearbyRepository {
    companion object {
        const val SERVICE_ID = "ar.edu.itba.pam.nearchatter"
    }

    private val tag = "NearbyRepository"
    @VisibleForTesting
    val hwIdDevices: MutableMap<String, Device> = HashMap()
    private var disconnectedDeviceCallback: Consumer<Device>? = null
    private var connectedDeviceCallback: Consumer<Device>? = null
    private var messageCallback: Consumer<Message>? = null

    private var stopping = false
    private var acceptsConnections = false

    override fun openConnections(username: String) {
        if (stopping) {
            throw ConcurrentModificationException()
        }
        if (acceptsConnections) {
            return
        }

        acceptsConnections = true

        nearbyConnectionHandler.init(
            connectionsClient,
            username,
            { endpointId, otherHwId, username ->
                Log.i(tag, "Connected with: $endpointId -> $otherHwId (username: $username)")
                val device = Device(otherHwId, endpointId, username)
                hwIdDevices[otherHwId] = device
                connectedDeviceCallback?.accept(device)
            },
            { otherHwId, message ->
                Log.i(tag, "Received Message: $message - $otherHwId")
                messageCallback?.accept(
                    Message(
                        UUID.randomUUID().toString(),
                        otherHwId,
                        hwId,
                        message,
                        LocalDateTime.now()
                    )
                )
            },
            { otherHwId ->
                Log.i(tag, "Disconnected: $otherHwId")
                if (hwIdDevices.containsKey(otherHwId)) {
                    val device = hwIdDevices[otherHwId]!!
                    hwIdDevices.remove(otherHwId)
                    disconnectedDeviceCallback?.accept(device)
                }
            }
        )

        startAdvertising(username, nearbyConnectionHandler.createConnectionLifecycleCallback())
        startDiscovery(nearbyConnectionHandler.createEndpointDiscoveryCallback())
    }

    override fun sendMessage(message: Message) {
        if (!acceptsConnections) {
            throw IllegalStateException()
        }

        val device = hwIdDevices[message.getReceiverId()] ?: return
        Log.i(tag, "Sending Message: $message - ${device.getId()}")
        nearbyConnectionHandler.sendMessage(device.getEndpointId(), message.getPayload())
    }

    override fun closeConnections() {
        Log.i(tag, "Closing connections")
        acceptsConnections = false
        stopping = true

        stopDiscovery()
        stopAdvertising()

        hwIdDevices.clear()

        stopping = false
    }

    override fun setOnConnectCallback(callback: Consumer<Device>?) {
        this.connectedDeviceCallback = callback
    }

    override fun setOnDisconnectCallback(callback: Consumer<Device>?) {
        this.disconnectedDeviceCallback = callback
    }

    override fun setOnMessageCallback(callback: Consumer<Message>?) {
        this.messageCallback = callback
    }

    private fun startAdvertising(
        username: String,
        lifecycle: ConnectionLifecycleCallback
    ) {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .setConnectionType(ConnectionType.BALANCED)
            .build()

        connectionsClient.startAdvertising(username, SERVICE_ID, lifecycle, advertisingOptions)
    }

    private fun startDiscovery(
        discovery: EndpointDiscoveryCallback
    ) {
        val discoveryOptions = DiscoveryOptions.Builder()
            .setStrategy(Strategy.P2P_CLUSTER)
            .build()
        connectionsClient.startDiscovery(SERVICE_ID, discovery, discoveryOptions)
    }

    private fun stopAdvertising() {
        connectionsClient.stopAdvertising()
    }

    private fun stopDiscovery() {
        connectionsClient.stopDiscovery()
    }
}
