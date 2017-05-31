package us.cornus.rotlin.path

/**
 * Created by ejw on 5/30/17.
 */

import us.cornus.rotlin.shift
import us.cornus.rotlin.push

data class DJItem(val x : Int, val y : Int, val prev : DJItem?)

/**
 * @class Simplified Dijkstra's algorithm: all edges have a value of 1
 * @augments ROT.Path
 * @see ROT.Path
 */
class Dijkstra(toX : Int, toY : Int, options : PathOptions, passableCallback: PathCallback) : Path(toX, toY, options, passableCallback) {

    constructor(toX : Int, toY : Int, topology: Int = 8, passableCallback: PathCallback) : this(toX, toY, PathOptions(topology), passableCallback)

    constructor(toX: Int, toY : Int, passableCallback: PathCallback, options: PathOptions) : this(toX, toY, options, passableCallback)

    private val computed = HashMap<String, DJItem>()
    private val todo = ArrayList<DJItem>()

    init { add(toX, toY, null) }


    /**
     * Compute a path from a given point
     * @see Path#compute
     */
    override fun compute(fromX : Int, fromY : Int, callback : PathCallback) {
        val key = "$fromX,$fromY"
        if (key !in computed) { _compute(fromX, fromY) }
        if (key !in computed) { return }

        var item = computed[key]
        while (item != null) {
            callback(item.x, item.y)
            item = item.prev
        }
    }

    /**
     * Compute a non-cached value
     */
    private fun _compute(fromX : Int, fromY : Int) {
        while (todo.size != 0) {
            val item = todo.shift()!!
            if (item.x == fromX && item.y == fromY) { return }

            val neighbors = this._getNeighbors(item.x, item.y)

            for ((x, y) in neighbors) {
                val id = "$x,$y"
                if (id in computed) { continue } /* already done */
                add(x, y, item)
            }
        }
    }

   private fun add(x : Int, y : Int, prev : DJItem?) {
        val obj = DJItem(
            x = x,
            y = y,
            prev = prev
                )
        computed["$x,$y"] = obj
        todo.push(obj)
    }
}