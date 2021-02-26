package nl.avwie.common

import kotlinx.serialization.Serializable
import nl.avwie.common.jsinterop.UUIDAdapter

@Serializable(with = UUIDAsStringSerializer::class)
actual data class UUID(private val inner: String) {
    actual override fun toString(): String {
        return inner
    }

    actual override fun equals(other: Any?): Boolean {
        return inner.hashCode() == other.hashCode()
    }

    actual override fun hashCode(): Int {
        return inner.hashCode()
    }

    actual companion object {
        actual fun random(): UUID {
            return UUID(UUIDAdapter.v4())
        }

        actual fun fromString(input: String): UUID? {
            return if (UUIDAdapter.validate(input)) {
                UUID(input)
            } else {
                null
            }
        }
    }

}