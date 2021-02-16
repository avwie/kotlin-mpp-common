package nl.rjcoding.common

actual fun sleep(milliseconds: Long) {
    Thread.sleep(milliseconds)
}