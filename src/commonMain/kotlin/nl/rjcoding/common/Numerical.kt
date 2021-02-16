package nl.rjcoding.common

interface NumericalOps<T> {
    fun plus(left: T, right: T): T
    fun minus(left: T, right: T): T
    fun times(left: T, right: T): T
    fun div(left: T, right: T): T

    fun min(left: T, right: T): T
    fun max(left: T, right: T): T

    fun compare(left: T, right: T): Int

    val zero: T
    val unit: T
}

object Integral : NumericalOps<Int> {
    override fun plus(left: Int, right: Int): Int {
        return left + right
    }

    override fun minus(left: Int, right: Int): Int {
        return left - right
    }

    override fun times(left: Int, right: Int): Int {
        return left * right
    }

    override fun div(left: Int, right: Int): Int {
        return left / right
    }

    override fun min(left: Int, right: Int): Int {
        return kotlin.math.min(left, right)
    }

    override fun max(left: Int, right: Int): Int {
        return kotlin.math.max(left, right)
    }

    override fun compare(left: Int, right: Int): Int {
        return left.compareTo(right)
    }

    override val zero: Int = 0
    override val unit: Int = 1

    tailrec fun gcd(a: Int, b: Int): Int {
        if (a == b) return a
        if (b == 0) {
            if (a == 0) return 1
            else return a
        }
        else return gcd(b, a % b)
    }
}

object Rational : NumericalOps<Double> {
    override fun plus(left: Double, right: Double): Double {
        return left + right
    }

    override fun minus(left: Double, right: Double): Double {
        return left - right
    }

    override fun times(left: Double, right: Double): Double {
        return left * right
    }

    override fun div(left: Double, right: Double): Double {
        return left / right
    }

    override fun min(left: Double, right: Double): Double {
        return kotlin.math.min(left, right)
    }

    override fun max(left: Double, right: Double): Double {
        return kotlin.math.max(left, right)
    }

    override fun compare(left: Double, right: Double): Int {
        return left.compareTo(right)
    }

    override val zero: Double = 0.0
    override val unit: Double = 1.0
}