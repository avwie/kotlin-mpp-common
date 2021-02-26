package nl.avwie.common

import kotlin.js.Date

actual fun sleep(milliseconds: Long) {
    val now = Date.now()
    var current = Date.now()
    while (current - now < milliseconds) {
        current = Date.now()
    }
}