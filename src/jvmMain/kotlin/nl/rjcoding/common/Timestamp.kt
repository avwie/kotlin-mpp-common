package nl.rjcoding.common

actual object Timestamp {
    actual fun now(): Long = System.currentTimeMillis()
}