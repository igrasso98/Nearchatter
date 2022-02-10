package ar.edu.itba.pam.nearchatter.models

class Device(
    private val id: String,
    private val endpointId: String,
    private val username: String,
) {
    fun getId(): String {
        return id
    }

    fun getEndpointId(): String {
        return endpointId
    }

    fun getUsername(): String {
        return username
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Device

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
