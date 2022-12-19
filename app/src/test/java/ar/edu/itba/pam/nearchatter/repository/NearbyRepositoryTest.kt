package ar.edu.itba.pam.nearchatter.repository

import ar.edu.itba.pam.nearchatter.domain.Message
import ar.edu.itba.pam.nearchatter.models.Device
import ar.edu.itba.pam.nearchatter.test_utils.MockitoHelper
import com.google.android.gms.nearby.connection.ConnectionsClient
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.Mockito.*
import java.time.LocalDateTime

class NearbyRepositoryTest {
  @Test
  fun testOpenConnections() {
    val mockNearbyConnectionHandler = mock(INearbyConnectionHandler::class.java)
    val mockConnectionsClient = mock(ConnectionsClient::class.java)
    val repository = NearbyRepository("fakeHardwareId", mockNearbyConnectionHandler, mockConnectionsClient)

    repository.openConnections("fakeUsername")

    verify(mockNearbyConnectionHandler, times(1)).init(
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject()
    )
    verify(mockConnectionsClient, times(1)).startAdvertising(
      MockitoHelper.anyObject<String>(),
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject(),
    )
    verify(mockConnectionsClient, times(1)).startDiscovery(
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject(),
      MockitoHelper.anyObject(),
    )
  }

  @Test
  fun testOpenConnectionsMultipleTimesDoesntThrow() {
    val mockNearbyConnectionHandler = mock(INearbyConnectionHandler::class.java)
    val mockConnectionsClient = mock(ConnectionsClient::class.java)
    val repository = NearbyRepository("fakeHardwareId", mockNearbyConnectionHandler, mockConnectionsClient)

    repository.openConnections("fakeUsername")
    // repository does not throw when calling openConnections again
    repository.openConnections("fakeUsername")
  }

  @Test
  fun testSendMessageFailsIfNotOpen() {
    val mockNearbyConnectionHandler = mock(INearbyConnectionHandler::class.java)
    val mockConnectionsClient = mock(ConnectionsClient::class.java)
    val repository = NearbyRepository("fakeHardwareId", mockNearbyConnectionHandler, mockConnectionsClient)

    assertThrows(IllegalStateException::class.java) {
      repository.sendMessage(Message(1, "sender", "receiver", "content", LocalDateTime.now()))
    }
  }

  @Test
  fun testSendMessageReturnsIfNoDevice() {
    val mockNearbyConnectionHandler = mock(INearbyConnectionHandler::class.java)
    val mockConnectionsClient = mock(ConnectionsClient::class.java)
    val repository = NearbyRepository("fakeHardwareId", mockNearbyConnectionHandler, mockConnectionsClient)

    repository.openConnections("fakeUsername")

    val message = Message(1, "sender", "receiver", "content", LocalDateTime.now())
    repository.sendMessage(message)

    verify(mockNearbyConnectionHandler, times(0)).sendMessage("fakeEndpointId", "content")
  }

  @Test
  fun testSendMessageCallsHandler() {
    val mockNearbyConnectionHandler = mock(INearbyConnectionHandler::class.java)
    val mockConnectionsClient = mock(ConnectionsClient::class.java)
    val repository = NearbyRepository("fakeHardwareId", mockNearbyConnectionHandler, mockConnectionsClient)

    repository.openConnections("fakeUsername")
    repository.hwIdDevices["fakeHwId"] = Device("fakeHwId", "fakeEndpointId", "fakeUsername")

    val message = Message(1, "sender", "fakeHwId", "content", LocalDateTime.now())
    repository.sendMessage(message)

    verify(mockNearbyConnectionHandler, times(1)).sendMessage("fakeEndpointId", "content")
  }

  @Test
  fun testCloseConnectionCallsClient() {
    val mockNearbyConnectionHandler = mock(INearbyConnectionHandler::class.java)
    val mockConnectionsClient = mock(ConnectionsClient::class.java)
    val repository = NearbyRepository("fakeHardwareId", mockNearbyConnectionHandler, mockConnectionsClient)

    repository.openConnections("fakeUsername")
    repository.hwIdDevices["fakeHwId"] = Device("fakeHwId", "fakeEndpointId", "fakeUsername")

    val message = Message(1, "sender", "fakeHwId", "content", LocalDateTime.now())
    repository.sendMessage(message)

    verify(mockNearbyConnectionHandler, times(1)).sendMessage("fakeEndpointId", "content")
  }
}
