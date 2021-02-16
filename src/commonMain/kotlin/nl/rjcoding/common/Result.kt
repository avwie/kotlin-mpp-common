package nl.rjcoding.common

sealed class Result<out Ok, out Err> {
    data class Ok<out Ok>(val value: Ok) : Result<Ok, Nothing>()
    data class Err<out Err>(val reason: Err) : Result<Nothing, Err>()

    val isSuccess: Boolean = this is Result.Ok
    val isFailure = !isSuccess

    fun then(block: (Ok) -> Unit): Result<Ok, Err> {
        when (this) {
            is Result.Ok -> block(this.value)
            is Result.Err -> {
            }
        }
        return this
    }

    fun thenOrError(block: (Ok) -> Unit) {
        this.unwrap().let(block)
    }

    fun catch(block: (Err) -> Unit): Result<Ok, Err> {
        when (this) {
            is Result.Ok -> {}
            is Result.Err -> block(this.reason)
        }
        return this
    }

    fun <R> map(block: (Ok) -> R): Result<R, Err> {
        return when (this) {
            is Result.Ok -> Ok(block(this.value))
            is Result.Err -> this
        }
    }

    fun <R> flatMap(block: (Ok) -> Result<R, @UnsafeVariance Err>): Result<R, Err> {
        return when (this) {
            is Result.Ok -> block(this.value)
            is Result.Err -> this
        }
    }

    fun <R> propagate(block: (Err) -> R): Result<Ok, R> {
        return when (this) {
            is Result.Ok -> this
            is Result.Err -> Err(block(this.reason))
        }
    }

    fun unwrap(): Ok {
        return when (this) {
            is Result.Ok -> this.value
            is Result.Err -> throw Error("Can't continue with Err: ${this.reason}")
        }
    }

    fun <R> unwrap(block: (Ok) -> R): R = this.unwrap().let(block)

    fun unwrapOrNull(): Ok? {
        return when (this) {
            is Result.Ok -> this.value
            is Result.Err -> null
        }
    }

    fun <R> unwrapOrNull(block: (Ok) -> R): R? = this.unwrapOrNull()?.let(block)

    fun expect(): Err {
        return when (this) {
            is Result.Ok -> throw Error("Can't continue with Ok: ${this.value}")
            is Result.Err -> this.reason
        }
    }

    fun <R> expect(block: (Err) -> R): R = this.expect().let(block)

    fun expectOrNull(): Err? {
        return when (this) {
            is Result.Ok -> null
            is Result.Err -> this.reason
        }
    }

    fun <R> expectOrNull(block: (Err) -> R): R? = this.expectOrNull()?.let(block)

    companion object {
        fun <Err> Boolean.toResult(failure: Err): Result<Unit, Err> {
            return if (this) {
                Ok(Unit)
            } else {
                Err(failure)
            }
        }
    }
}