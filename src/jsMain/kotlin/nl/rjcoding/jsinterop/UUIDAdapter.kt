package nl.rjcoding.jsinterop

@JsModule("uuid")
@JsNonModule
external object UUIDAdapter {
    fun v4(): String
    fun validate(input: String): Boolean
}