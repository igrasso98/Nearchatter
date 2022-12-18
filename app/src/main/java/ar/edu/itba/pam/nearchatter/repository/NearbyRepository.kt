package ar.edu.itba.pam.nearchatter.repository

import androidx.annotation.VisibleForTesting
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.models.Device
import com.google.android.gms.nearby.connection.*
import java.time.LocalDate
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
                println("Connected with: $endpointId -> $otherHwId (username: $username)")
                val device = Device(otherHwId, endpointId, username)
                hwIdDevices[otherHwId] = device
                connectedDeviceCallback?.accept(device)
            },
            { otherHwId, message ->
                println("Message: $message - $otherHwId")
                messageCallback?.accept(Message(
                    null,
                    otherHwId,
                    hwId,
                    message,
                    LocalDate.now()
                ))
            },
            { otherHwId ->
                println("Disconnected: $otherHwId")
                disconnectedDeviceCallback?.accept(hwIdDevices.remove(otherHwId)!!)
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
        nearbyConnectionHandler.sendMessage(device.getEndpointId(), message.getPayload())
    }

    override fun closeConnections() {
        acceptsConnections = false
        stopping = true

        stopDiscovery()
        stopAdvertising()

        hwIdDevices.clear()

        stopping = false
    }

    override fun setOnConnectCallback(callback: Consumer<Device>?) {
        this.disconnectedDeviceCallback = callback
    }

    override fun setOnDisconnectCallback(callback: Consumer<Device>?) {
        this.connectedDeviceCallback = callback
    }

    override fun setOnMessageCallback(callback: Consumer<Message>?) {
        this.messageCallback = callback
    }

    private fun startAdvertising(
        username: String,
        lifecycle: ConnectionLifecycleCallback
    ) {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_STAR)
            .setDisruptiveUpgrade(false)
            .build()

        connectionsClient.startAdvertising(username, SERVICE_ID, lifecycle, advertisingOptions)
    }

    private fun startDiscovery(
        discovery: EndpointDiscoveryCallback
    ) {
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
        connectionsClient.startDiscovery(SERVICE_ID, discovery, discoveryOptions)
    }

    private fun stopAdvertising() {
        connectionsClient.stopAdvertising()
    }

    private fun stopDiscovery() {
        connectionsClient.stopDiscovery()
    }
}
