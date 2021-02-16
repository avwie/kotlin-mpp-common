package nl.rjcoding.common

fun Long.toByteArray() : ByteArray {
    val arr = ByteArray(Long.SIZE_BYTES)
    var l = this
    (Long.SIZE_BYTES - 1 downTo 0).forEach { i ->
        arr[i] = (l and 0xFF).toByte()
        l = l shr Byte.SIZE_BITS
    }
    return arr
}

fun ByteArray.toLong(): Long {
    var l = 0L
    this.forEachIndexed { index, byte ->
        l = l shl Byte.SIZE_BITS
        l = l or (byte.toLong() and 0xFF)
    }
    return l
}