package ar.edu.itba.pam.nearchatter.services

import android.annotation.SuppressLint
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.domain.User
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.INearbyRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.function.Consumer


class NearbyService(
    private val nearbyRepository: INearbyRepository,
    private val userRepository: IUserRepository,
    private val messageRepository: IMessageRepository,
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

    override fun sendMessage(message: Message) {
        nearbyRepository.sendMessage(message)
        messageRepository.addMessage(message)
    }

    override fun closeConnections() {
        nearbyRepository.closeConnections()
    }

    @SuppressLint("CheckResult")
    private fun setUpNearbyRepository() {
        nearbyRepository.setOnConnectCallback { device ->
            val user = User(device.getId(), device.getUsername(), true)
            GlobalScope.async {
                userRepository.addUser(user).subscribe { _ ->
                    connectedDeviceCallback?.accept(user)
                }
            }
        }

        nearbyRepository.setOnMessageCallback { message ->
            messageRepository.addMessage(message).subscribe { dbMessage ->
                userRepository.setLastMessage(dbMessage)
            }
        }

        nearbyRepository.setOnDisconnectCallback { device ->
            disconnectedDeviceCallback?.accept(User(device.getId(), device.getUsername(), false))
        }
    }
}

