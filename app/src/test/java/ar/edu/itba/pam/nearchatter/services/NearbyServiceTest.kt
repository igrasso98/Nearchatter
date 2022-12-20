package ar.edu.itba.pam.nearchatter.services

import ar.edu.itba.pam.nearchatter.di.NearchatterContainerLocator
import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.repository.IMessageRepository
import ar.edu.itba.pam.nearchatter.repository.INearbyRepository
import ar.edu.itba.pam.nearchatter.repository.IUserRepository
import ar.edu.itba.pam.nearchatter.test_utils.MockitoHelper
import ar.edu.itba.pam.nearchatter.test_utils.TestingUtils
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.openMocks
import java.time.LocalDateTime
import java.time.ZoneOffset

class NearbyServiceTest {
  @Captor
  private lateinit var messageCaptor: ArgumentCaptor<Message>

  @Before
  fun setUp() {
    openMocks(this)
    TestingUtils.setNearchatterContainer()
  }

  @Test
  fun openConnections_callsNearbyRepositoryOpenConnections() {
    val nearbyRepository = mock(INearbyRepository::class.java)
    val userRepository = mock(IUserRepository::class.java)
    val messageRepository = mock(IMessageRepository::class.java)
    val nearbyService = NearbyService("sender", nearbyRepository, userRepository, messageRepository, NearchatterContainerLocator.container!!.getSchedulerProvider())

    nearbyService.openConnections("test_username")
    verify(nearbyRepository).openConnections("test_username")
  }

  @Test
  fun sendMessage_callsNearbyRepositoryAndSavesMessage() {
    val nearbyRepository = mock(INearbyRepository::class.java)
    val userRepository = mock(IUserRepository::class.java)
    `when`(userRepository.setLastMessage(MockitoHelper.anyObject())).thenReturn(Single.just(Unit))
    val messageRepository = mock(IMessageRepository::class.java)
    `when`(messageRepository.addMessage(MockitoHelper.anyObject())).thenReturn(Single.just(Unit))

    val nearbyService = NearbyService("sender", nearbyRepository, userRepository, messageRepository, NearchatterContainerLocator.container!!.getSchedulerProvider())

    nearbyService.sendMessage("content", "receiver")

    val localDate = LocalDateTime.now()
    verify(nearbyRepository).sendMessage(MockitoHelper.capture(messageCaptor))
    verify(messageRepository).addMessage(MockitoHelper.capture(messageCaptor))

    assert(messageCaptor.allValues[0] == messageCaptor.allValues[1])
    assert(messageCaptor.allValues[0].getSenderId() == "sender")
    assert(messageCaptor.allValues[0].getReceiverId() == "receiver")
    assert(messageCaptor.allValues[0].getPayload() == "content")
    assert(messageCaptor.allValues[0].getSendAt().toEpochSecond(ZoneOffset.UTC) >= localDate.toEpochSecond(ZoneOffset.UTC))
  }
}
