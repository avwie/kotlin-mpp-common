package nl.avwie.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

interface TimeStamped<Ts, Data> {
    val timestamp: Ts
    val data: Data
}

expect object Timestamp {
    fun now(): Long
}

@Serializable(with = DistributedTimestampAsLongSerializer::class) @SerialName("timestamp_distributed")
data class DistributedTimestamp(val timestamp: Long, val index: Int, val shard: Int) : Comparable<DistributedTimestamp> {

    override fun compareTo(other: DistributedTimestamp): Int {
        if (timestamp.compareTo(other.timestamp) != 0) return timestamp.compareTo(other.timestamp)
        if (index.compareTo(other.index) != 0) return index.compareTo(other.index)
        return shard.compareTo(other.shard)
    }

    fun toLong(): Long {
        var ts = timestamp
        ts = ts shl INDEX_BITS
        ts += index
        ts = ts shl SHARD_BITS
        ts += shard
        return ts
    }

    override fun toString(): String = toLong().toString()

    companion object {
        const val INDEX_BITS = 10
        const val SHARD_BITS = 10
        const val OFFSET = 1609455600000L // 2021-01-01 00:00

        const val INDEX_MAX = (1 shl INDEX_BITS)
        const val SHARD_MAX = (1 shl SHARD_BITS)
        const val SHARD_MASK = SHARD_MAX - 1
        const val INDEX_MASK =  INDEX_MAX - 1

        fun fromLong(timestamp: Long): DistributedTimestamp {
            val shard = (timestamp and SHARD_MASK.toLong()).toInt()
            val index = ((timestamp shr SHARD_BITS) and INDEX_MASK.toLong()).toInt()
            return DistributedTimestamp(timestamp shr (INDEX_BITS + SHARD_BITS), index, shard)
        }
    }
}

class DistributedTimestampGenerator(private val shard: Int, val blocking: Boolean = true) : Generator<DistributedTimestamp> {
    private var latestTimestamp = 0L
    private var currentIndex = -1

    override fun generate(): DistributedTimestamp {
        val indexBits = DistributedTimestamp.INDEX_BITS
        val shardBits = DistributedTimestamp.SHARD_BITS
        val totalBits = indexBits + shardBits

        var timestamp = ((Timestamp.now() - DistributedTimestamp.OFFSET) shl totalBits) shr totalBits
        if (timestamp == latestTimestamp && currentIndex == DistributedTimestamp.INDEX_MAX - 1) {
            sleep(1)
            timestamp = ((Timestamp.now() - DistributedTimestamp.OFFSET) shl totalBits) shr totalBits
        }

        if (timestamp != latestTimestamp) currentIndex = -1
        return DistributedTimestamp(timestamp, ++currentIndex, shard).also {
            latestTimestamp = timestamp
            currentIndex %= DistributedTimestamp.INDEX_MAX
        }
    }
}

object DistributedTimestampAsLongSerializer : KSerializer<DistributedTimestamp> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DistributedTimestamp", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: DistributedTimestamp) {
        encoder.encodeLong(value.toLong())
    }

    override fun deserialize(decoder: Decoder): DistributedTimestamp {
        return DistributedTimestamp.fromLong(decoder.decodeLong())
    }
}
