package ar.edu.itba.pam.nearchatter.services

import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.INearbyRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.time.LocalDate

class NearbyServiceTest {
  @Test
  fun openConnections_callsNearbyRepositoryOpenConnections() {
    val nearbyRepository = mock(INearbyRepository::class.java)
    val userRepository = mock(IUserRepository::class.java)
    val messageRepository = mock(IMessageRepository::class.java)
    val nearbyService = NearbyService(nearbyRepository, userRepository, messageRepository)

    nearbyService.openConnections("test_username")
    verify(nearbyRepository).openConnections("test_username")
  }

  @Test
  fun sendMessage_callsNearbyRepositoryAndSavesMessage() {
    val nearbyRepository = mock(INearbyRepository::class.java)
    val userRepository = mock(IUserRepository::class.java)
    val messageRepository = mock(IMessageRepository::class.java)
    val nearbyService = NearbyService(nearbyRepository, userRepository, messageRepository)

    val message = Message(null, "sender", "receiver", "content", LocalDate.now())
    nearbyService.sendMessage(message)
    verify(nearbyRepository).sendMessage(message)
    verify(messageRepository).addMessage(message)
  }
}
