package nl.avwie.common

import kotlinx.serialization.Serializable
import java.lang.IllegalArgumentException

@Serializable(with = UUIDAsStringSerializer::class)
actual data class UUID(private val inner: java.util.UUID) {

    actual override fun toString(): String {
        return inner.toString()
    }

    actual override fun equals(other: Any?): Boolean {
        val res = inner.hashCode() == other.hashCode()
        return inner.hashCode() == other.hashCode()
    }

    actual override fun hashCode(): Int {
        return inner.hashCode()
    }

    actual companion object {
        actual fun random(): UUID {
            return UUID(java.util.UUID.randomUUID())
        }

        actual fun fromString(input: String): UUID? {
            try {
                return UUID(java.util.UUID.fromString(input))
            } catch (ex: IllegalArgumentException) {
                return null
            }
        }
    }

}