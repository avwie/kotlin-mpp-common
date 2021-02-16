package nl.rjcoding.common

fun interface Generator<T> {
    fun generate(): T

    companion object {
        val UUID = Generator { nl.rjcoding.common.UUID.random() }
    }
}