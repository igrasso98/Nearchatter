package ar.edu.itba.pam.nearchatter.services

interface INearbyService {
    /**
     * Broadcasts and searches for devices with Nearchatter app open
     * For each one of them, it tries to connect to them.
     * @throws ConcurrentModificationException if the service is stopping
     */
    fun openConnections(username: String)

    fun sendMessage(message: String, receiverId: String)

    fun closeConnections()
}
