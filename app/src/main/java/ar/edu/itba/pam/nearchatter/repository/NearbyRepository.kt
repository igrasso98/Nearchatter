package ar.edu.itba.pam.nearchatter.repository

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.models.Device
import ar.edu.itba.pam.nearchatter.repository.NearbyConnectionHandler.Companion.MAGIC_PREFIX
import ar.edu.itba.pam.nearchatter.repository.NearbyConnectionHandler.Companion.MESSAGE_PREFIX
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import java.time.LocalDate
import java.util.function.Consumer

class NearbyRepository(
    context: Context,
    private val userRepository: UserRepository,
    private val disconnectedDeviceCallback: Consumer<Device>,
    private val connectedDeviceCallback: Consumer<Device>,
    private val messageCallback: Consumer<Message>,
) :
    INearbyRepository {
    companion object {
        const val SERVICE_ID = "ar.edu.itba.pam.nearchatter"
    }

    private val hwId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    private val connectionsClient = Nearby.getConnectionsClient(context)
    private var nearbyConnectionHandler: NearbyConnectionHandler? = null
    private val hwIdDevices: MutableMap<String, Device> = HashMap()

    private var stopping = false
    private var acceptsConnections = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun openConnections(username: String) {
        if (stopping) {
            throw ConcurrentModificationException()
        }
        if (nearbyConnectionHandler != null) {
            return
        }

        acceptsConnections = true

        nearbyConnectionHandler = NearbyConnectionHandler(
            connectionsClient,
            hwId,
            username,
            { endpointId, otherHwId, username ->
                println("Connected with: $endpointId -> $otherHwId (username: $username)")
                val device = Device(otherHwId, endpointId, username)
                hwIdDevices[otherHwId] = device
                connectedDeviceCallback.accept(device)
            },
            { otherHwId, message ->
                println("Message: $message - $otherHwId")
                messageCallback.accept(Message(
                    null,
                    otherHwId,
                    hwId,
                    message,
                    LocalDate.now()
                ))
            },
            { otherHwId ->
                println("Disconnected: $otherHwId")
                disconnectedDeviceCallback.accept(hwIdDevices.remove(otherHwId)!!)
            }
        )

        startAdvertising(username, nearbyConnectionHandler!!.ConnectionLifecycle())
        startDiscovery(nearbyConnectionHandler!!.EndpointDiscovery())
    }

    override fun sendMessage(message: Message) {
        if (!acceptsConnections) {
            throw IllegalStateException()
        }

        val device = hwIdDevices[message.getReceiverId()] ?: return
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

        hwIdDevices.clear()

        stopping = false
    }

    private fun startAdvertising(
        username: String,
        lifecycle: ConnectionLifecycleCallback
    ) {
        val advertisingOptions: AdvertisingOptions = AdvertisingOptions.Builder()
            .setStrategy(Strategy.P2P_STAR)
            .setDisruptiveUpgrade(false)
            .build()

        connectionsClient
            .startAdvertising(username, SERVICE_ID, lifecycle, advertisingOptions)
            .addOnSuccessListener { println("Accepting User") }
            .addOnFailureListener { throw it }
    }

    private fun startDiscovery(
        discovery: EndpointDiscoveryCallback
    ) {
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(Strategy.P2P_STAR).build()
        connectionsClient
            .startDiscovery(SERVICE_ID, discovery, discoveryOptions)
            .addOnSuccessListener { println("Accepting User") }
            .addOnFailureListener { throw it }
    }

    private fun stopAdvertising() {
        connectionsClient.stopAdvertising()
    }

    private fun stopDiscovery() {
        connectionsClient.stopDiscovery()
    }
}
