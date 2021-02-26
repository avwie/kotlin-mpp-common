package nl.avwie.common

enum class RotationalOrientation {
    Collinear,
    Clockwise,
    CounterClockwise
}

enum class Orientation {
    Horizontal,
    Vertical;

    fun directions(): Pair<Direction, Direction> = when (this) {
        Horizontal -> Direction.East to Direction.West
        Vertical -> Direction.North to Direction.South
    }
}

enum class Direction(val angle: Int) {
    North(0),
    East(90),
    South(180),
    West(270);

    fun opposite(): Direction = when (this) {
        North -> South
        East -> West
        South -> North
        West -> East
    }

    fun orientation(): Orientation = when (this) {
        North, South -> Orientation.Vertical
        East, West -> Orientation.Horizontal
    }

    companion object {
        fun fromAngle(angle: Int): Direction? = when (normalizeAngle(angle)) {
            0 -> North
            90 -> East
            180 -> South
            270 -> West
            else -> null
        }

        private tailrec fun normalizeAngle(angle: Int): Int = when {
            angle < 0 -> normalizeAngle(angle + 360)
            angle >= 360 -> normalizeAngle(angle % 360)
            else -> angle
        }
    }
}

interface HasArea<T> {
    val area: Area<T>
}

val <T> NumericalOps<T>.geometry: GeometryProvider<T>
    get() = GeometryProvider(this)

class GeometryProvider<T>(private val ops: NumericalOps<T>) {
    val vector2D = Vector2DProvider(ops)
    val line = LineProvider(ops)
    val area = AreaProvider(ops)
    val rectangle = RectangleProvider(ops)
}

class Vector2DProvider<T>(private val ops: NumericalOps<T>) {
    val zero = Vector2D(ops.zero, ops.zero, ops)
    val unit = Vector2D(ops.unit, ops.unit, ops)

    operator fun invoke(x: T, y: T): Vector2D<T> {
        return Vector2D(x, y, ops)
    }

    fun orientation(p: Vector2D<T>, q: Vector2D<T>, r: Vector2D<T>): RotationalOrientation {
        val a = ops.minus(q.y, p.y)
        val b = ops.minus(r.x, q.x)
        val c = ops.minus(q.x, p.x)
        val d = ops.minus(r.y, q.y)
        val z = ops.minus(ops.times(a, b), ops.times(c, d))

        return when (z) {
            ops.zero -> RotationalOrientation.Collinear
            ops.max(z, ops.zero) -> RotationalOrientation.Clockwise
            else -> RotationalOrientation.CounterClockwise
        }
    }
}

class LineProvider<T>(private val ops: NumericalOps<T>) {
    operator fun invoke(start: Vector2D<T>, end: Vector2D<T>): Line<T> = Line(start, end, ops)
    operator fun invoke(x0: T, y0: T, x1: T, y1: T): Line<T> = Line(x0, y0, x1, y1, ops)
}

class AreaProvider<T>(private val ops: NumericalOps<T>) {
    val empty = Area(ops.zero, ops.zero)
    val unit = Area(ops.unit, ops.unit)

    operator fun invoke(width: T, height: T) = Area(width, height)
}

class RectangleProvider<T>(private val ops: NumericalOps<T>) {
    operator fun invoke(origin: Vector2D<T>, area: Area<T>) = Rectangle(origin, area, ops)
    operator fun invoke(x: T, y: T, width: T, height: T) = Rectangle(x, y, width, height, ops)
}

data class Vector2D<T>(val x: T, val y: T, private val ops: NumericalOps<T>) {
    operator fun plus(other: Vector2D<T>) = Vector2D(ops.plus(x, other.x), ops.plus(y, other.y), ops)
    operator fun minus(other: Vector2D<T>) = Vector2D(ops.minus(x, other.x), ops.minus(y, other.y), ops)
}

data class Line<T>(val start: Vector2D<T>, val end: Vector2D<T>, private val ops: NumericalOps<T>) {
    constructor(x0: T, y0: T, x1: T, y1: T, ops: NumericalOps<T>): this(Vector2D(x0, y0, ops), Vector2D(x1, y1, ops), ops)

    fun intersects(other: Line<T>): Boolean {
        val vec = Vector2DProvider(ops)

        val p1 = this.start
        val q1 = this.end
        val p2 = other.start
        val q2 = other.end

        val o1 = vec.orientation(p1, q1, p2)
        val o2 = vec.orientation(p1, q1, q2)
        val o3 = vec.orientation(p2, q2, p1)
        val o4 = vec.orientation(p2, q2, q1)

        return (o1 != o2 && o3 != o4)
    }
}

data class Area<T>(val width: T, val height: T)

data class Rectangle<T>(val origin: Vector2D<T>, val area: Area<T>, private val ops: NumericalOps<T>) {

    constructor(x: T, y: T, width: T, height: T, ops: NumericalOps<T>): this(Vector2D(x, y, ops), Area(width, height), ops)

    val x0 = origin.x
    val y0 = origin.y
    val x1 = ops.plus(origin.x, area.width)
    val y1 = ops.plus(origin.y, area.height)
    val width = area.width
    val height = area.height

    fun move(d: Vector2D<T>) : Rectangle<T> = copy(origin = origin + d)

    operator fun plus(other: Rectangle<T>): Rectangle<T> {
        val u0 = ops.min(x0, other.x0)
        val v0 = ops.min(y0, other.y0)
        val u1 = ops.max(x1, other.x1)
        val v1 = ops.max(y1, other.y1)

        val width = ops.minus(u1, u0)
        val height = ops.min(v1, v0)
        return Rectangle(u0, v0, width, height, ops)
    }
}