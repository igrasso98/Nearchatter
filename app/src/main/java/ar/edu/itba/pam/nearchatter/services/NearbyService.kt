package ar.edu.itba.pam.nearchatter.services

import android.annotation.SuppressLint
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.INearbyRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.utils.schedulers.SchedulerProvider
import java.time.LocalDate
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
        this.disconnectedDeviceCallback = callback
    }

    override fun setOnDisconnectCallback(callback: Consumer<User>?) {
        this.connectedDeviceCallback = callback
    }

    override fun openConnections(username: String) {
        nearbyRepository.openConnections(username)
    }

    override fun sendMessage(message: String, receiverId: String) {
        val messageObj = Message(null, hwId, receiverId, message, LocalDate.now())
        nearbyRepository.sendMessage(messageObj)
        schedulerProvider.io().scheduleDirect {
            println("To save sent message: $messageObj")
            messageRepository.addMessage(messageObj)
        }
            // TODO: SET LAST MESSAGE
    }

    override fun closeConnections() {
        nearbyRepository.closeConnections()
    }

    @SuppressLint("CheckResult")
    private fun setUpNearbyRepository() {
        nearbyRepository.setOnConnectCallback { device ->
            val user = User(device.getId(), device.getUsername(), true)
            schedulerProvider.io().scheduleDirect {
                userRepository.addUser(user).subscribe { _ ->
                    connectedDeviceCallback?.accept(user)
                }
            }
        }

        nearbyRepository.setOnMessageCallback { message ->
            schedulerProvider.io().scheduleDirect {
                messageRepository.addMessage(message).subscribe { dbMessage ->
                    schedulerProvider.io().scheduleDirect {
                        userRepository.setLastMessage(dbMessage)
                    }
                }
            }
        }

        nearbyRepository.setOnDisconnectCallback { device ->
            disconnectedDeviceCallback?.accept(User(device.getId(), device.getUsername(), false))
        }
    }
}

