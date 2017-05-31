package us.cornus.rotlin.path

/**
 * Created by ejw on 5/30/17.
 */

import us.cornus.rotlin.ROT
import us.cornus.rotlin.push

typealias PathCallback = (x : Int, y : Int) -> Boolean

data class PathOptions(val topology : Int = 8)

class IllegalTopologyException(msg : String) : Exception(msg)

data class PLoc(val x : Int, val y : Int)

/**
 * @class Abstract pathfinder
 * @param {int} toX Target X coord
 * @param {int} toY Target Y coord
 * @param {function} passableCallback Callback to determine map passability
 * @param {object} [options]
 * @param {int} [options.topology=8]
 */
abstract class Path(val toX : Int, val toY : Int, val options : PathOptions, val passableCallback : PathCallback) {

    constructor(toX : Int, toY : Int, topology: Int = 8, passableCallback: PathCallback) : this(toX, toY, PathOptions(topology), passableCallback)

    constructor(toX: Int, toY : Int, passableCallback: PathCallback, options: PathOptions) : this(toX, toY, options, passableCallback)

    protected var fromX : Int? = null
    protected var fromY : Int? = null

    protected val dirs = _calculateDirs(options.topology)

    /**
     * Compute a path from a given point
     * @param {int} fromX
     * @param {int} fromY
     * @param {function} callback Will be called for every path item with arguments "x" and "y"
     */
    abstract fun compute(fromX : Int, fromY : Int, callback : PathCallback)

    protected fun _getNeighbors(cx: Int, cy: Int): ArrayList<PLoc> {
        val result = ArrayList<PLoc>()
        if (dirs != null) {
            for (dir in dirs) {
                val x = cx + dir[0]
                val y = cy + dir[1]

                if (!passableCallback(x, y)) {
                    continue
                }
                result.push(PLoc(x, y))
            }
        }
        return result
    }

       companion object {
        private fun _calculateDirs(topology: Int): Array<Array<Int>>? {
            val key = "$topology"

            val dirs = if (key in ROT.DIRS) ROT.DIRS[key] else null
            if (dirs != null) {
                return if (topology == 8) { /* reorder dirs for more aesthetic result (vertical/horizontal first) */
                    arrayOf(
                            dirs[0],
                            dirs[2],
                            dirs[4],
                            dirs[6],
                            dirs[1],
                            dirs[3],
                            dirs[5],
                            dirs[7]
                    )
                } else {
                    dirs
                }
            } else
                throw IllegalTopologyException("No support for topology $key")

        }
    }
}