package ar.edu.itba.pam.nearchatter.services

import android.annotation.SuppressLint
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.INearbyRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer


class NearbyService(
    private val hwId: String,
    private val nearbyRepository: INearbyRepository,
    private val userRepository: IUserRepository,
    private val messageRepository: IMessageRepository,
    private val schedulerProvider: SchedulerProvider,
) : INearbyService {
    private var disconnectedDeviceCallback: Consumer<User>? = null
    private var connectedDeviceCallback: Consumer<User>? = null

    init {
        setUpNearbyRepository()
    }

    override fun setOnConnectCallback(callback: Consumer<User>?) {
        this.connectedDeviceCallback = callback
    }

    override fun setOnDisconnectCallback(callback: Consumer<User>?) {
        this.disconnectedDeviceCallback = callback
    }

    override fun openConnections(username: String) {
        nearbyRepository.openConnections(username)
    }

    @SuppressLint("CheckResult")
    override fun sendMessage(message: String, receiverId: String) {
        val messageObj = Message(UUID.randomUUID().toString(), hwId, receiverId, message, LocalDateTime.now())
        nearbyRepository.sendMessage(messageObj)
        schedulerProvider.io().scheduleDirect {
            messageRepository.addMessage(messageObj).subscribe { _ ->
                userRepository.setLastMessage(messageObj).subscribe()
            }
        }
    }

    override fun closeConnections() {
        nearbyRepository.closeConnections()
    }

    @SuppressLint("CheckResult")
    private fun setUpNearbyRepository() {
        nearbyRepository.setOnConnectCallback { device ->
            val user = User(device.getId(), device.getUsername())
            schedulerProvider.io().scheduleDirect {
                userRepository.addUser(user).subscribe()
            }
            connectedDeviceCallback?.accept(user)
        }

        nearbyRepository.setOnMessageCallback { message ->
            schedulerProvider.io().scheduleDirect {
                messageRepository.addMessage(message).subscribe { _ ->
                    userRepository.setLastMessage(message).subscribe()
                }
            }
        }

        nearbyRepository.setOnDisconnectCallback { device ->
            disconnectedDeviceCallback?.accept(User(device.getId(), device.getUsername()))
        }
    }
}

