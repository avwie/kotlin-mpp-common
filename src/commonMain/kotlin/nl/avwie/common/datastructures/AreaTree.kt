package nl.avwie.common.datastructures

import nl.avwie.common.*
import kotlin.math.max

sealed class AreaTree<T> : HasArea<Int> {

    data class Item<T>(val item: T, override val area: Area<Int>): AreaTree<T>()

    class Container<T>: AreaTree<T>() {

        private var cursor = Integral.geometry.vector2D.zero
        private var currentArea = Integral.geometry.area.empty
        private var subTrees : MutableMap<Vector2D<Int>, AreaTree<T>> = mutableMapOf()

        override val area: Area<Int> get() = currentArea
        val children: Sequence<Pair<Vector2D<Int>, AreaTree<T>>> = subTrees.asSequence().map { it.toPair() }

        enum class AdditionMode {
            AppendToRow,
            NewRow
        }

        fun add(tree: AreaTree<T>, additionMode: AdditionMode, offset: Vector2D<Int> = Integral.geometry.vector2D.zero) {
            when (additionMode) {
                AdditionMode.AppendToRow -> {
                    cursor = cursor.copy(x = currentArea.width + offset.x)
                    currentArea = currentArea.copy(
                        width = currentArea.width + tree.area.width,
                        height = max(currentArea.height, tree.area.height + cursor.y)
                    )
                }

                AdditionMode.NewRow -> {
                    cursor = cursor.copy(x = 0, y = currentArea.height + offset.y)
                    currentArea = currentArea.copy(
                        width = max(currentArea.width, tree.area.width + cursor.x),
                        height = currentArea.height + tree.area.height
                    )
                }
            }
            subTrees[cursor] = tree
        }
    }
}