package us.cornus.rotlin.noise

/**
 * Created by ejw on 5/30/17.
 */

abstract class Noise {
    abstract fun get(x : Double, y : Double) : Double

    fun get(x : Int, y : Int) : Double = get(x.toDouble(), y.toDouble())

    fun get(x : Long, y : Long) : Double = get(x.toDouble(), y.toDouble())
}