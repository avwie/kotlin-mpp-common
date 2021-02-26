package nl.avwie.common

fun interface Generator<T> {
    fun generate(): T

    companion object {
        val UUID = Generator { nl.avwie.common.UUID.random() }
    }
}