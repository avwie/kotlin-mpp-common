package nl.avwie.common

actual fun sleep(milliseconds: Long) {
    Thread.sleep(milliseconds)
}