package nl.avwie.common

actual object Timestamp {
    actual fun now(): Long = System.currentTimeMillis()
}