package nl.avwie.common.jsinterop

@JsModule("uuid")
@JsNonModule
external object UUIDAdapter {
    fun v4(): String
    fun validate(input: String): Boolean
}