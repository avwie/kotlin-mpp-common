package nl.rjcoding.common

import kotlin.js.Date

actual object Timestamp {
    actual fun now(): Long {
        return Date().getTime().toLong()
    }
}