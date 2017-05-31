package us.cornus.rotlin.path

/**
 * Created by ejw on 5/30/17.
 */

import us.cornus.rotlin.shift
import us.cornus.rotlin.push

data class APItem(val x: Int, val y : Int, val prev : APItem?, val g : Int, val h : Int)
/**
 * @class Simplified A* algorithm: all edges have a value of 1
 * @augments ROT.Path
 * @see ROT.Path
 */
class AStar(toX : Int, toY : Int, options : PathOptions, passableCallback: PathCallback) : Path(toX, toY, options, passableCallback) {

    constructor(toX : Int, toY : Int, topology: Int = 8, passableCallback: PathCallback) : this(toX, toY, PathOptions(topology), passableCallback)

    constructor(toX: Int, toY : Int, passableCallback: PathCallback, options: PathOptions) : this(toX, toY, options, passableCallback)

    val todo = ArrayList<APItem>()
    val done = HashMap<String, APItem>()

    init {
        fromX = null
        fromY = null
    }

    /**
     * Compute a path from a given point
     * @see Path#compute
     */
    override fun compute(fromX : Int, fromY : Int, callback : PathCallback) {
        todo.clear()
        done.clear()
        this.fromX = fromX
        this.fromY = fromY
        add(toX, toY, null)

        while (todo.isNotEmpty()) {
            val item = todo.shift()!!
            if (item.x == fromX && item.y == fromY) { break }
            val neighbors : ArrayList<PLoc> = _getNeighbors(item.x, item.y)

            for ((x, y) in neighbors) {
                val id = "$x,$y"
                if (id in done) { continue }
                add(x, y, item)
            }
        }

        var item: APItem? = done["$fromX,$fromY"] ?: return

        while (item != null) {
            callback(item.x, item.y)
            item = item.prev
        }
    }

    private fun add(x :Int , y : Int, prev : APItem?) {
        val h = distance(x, y)
        val obj = APItem(
            x = x,
            y = y,
            prev = prev,
            g  = if (prev != null)  prev.g+1 else 0,
            h = h
        )
        done["$x,$y"] = obj

        /* insert into priority queue */

        val f = obj.g + obj.h
        for (i in todo.indices) {
            val item = todo[i]
            val itemF = item.g + item.h
            if (f < itemF || (f == itemF && h < item.h)) {
                todo.add(i, obj)
                return
            }
        }

        todo.push(obj)
    }

   private fun distance(x : Int, y : Int) : Int {
       val fx = fromX ?: 0
       val fy = fromY ?: 0
       when (options.topology) {
            4 -> return (Math.abs(x-(fx)) + Math.abs(y-(fy)))

            6 -> {
                val dx = Math.abs(x - fx)
                val dy = Math.abs(y - fy)
                return dy + Math.max(0, (dx - dy) / 2)
            }

            8 -> return Math.max(Math.abs(x-fx), Math.abs(y-fy))

            else -> throw IllegalTopologyException("Illegal topology")
        }
    }
}