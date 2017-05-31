/**
 * Created by ejw on 5/27/17.
 */

package us.cornus.rotlin

data class RNGState(val s0: Double, val s1 : Double, val s2 : Double, val c : Double)

/**
 * @namespace
 * This code is an implementation of Alea algorithm; (C) 2010 Johannes Baagøe.
 * Alea is licensed according to the http://en.wikipedia.org/wiki/MIT_License.
 */
//Should be sealed but that makes Idea very angry
open class RNGType(private var s0: Double = 0.0, private var s1: Double = 0.0, private var s2: Double = 0.0, private var c: Double = 0.0) {
    private val frac =  2.3283064365386963e-10
    var seed = 0.0
        /**
         * @returns {number}
         */
        get() = field
        /**
         * @param {number} seed Seed the number generator
         */
        set(iSeed) : Unit {
            var nSeed = if (iSeed < 1) { 1/ iSeed
            } else {
                iSeed
            }

            field = nSeed
            s0 = (nSeed.toLong() ushr 0) * frac

            nSeed = ((nSeed*69069 + 1).toLong() ushr 0).toDouble()
            this.s1 = nSeed * frac

            nSeed = ((nSeed*69069 + 1).toLong() ushr 0).toDouble()
            s2 = nSeed * frac

            c = 1.0
        }

    /**
     * @returns {float} Pseudo-random value [0,1), uniformly distributed
     */
    fun getUniform() : Double {
        val t = 2091639 * s0 + c * frac
        s0 = s1
        s1 = s2
        c = (t.toLong() or 0).toDouble()
        s2 = t - c
        return s2
    }

    /**
     * @param {int} lowerBound The lower end of the range to return a value from, inclusive
     * @param {int} upperBound The upper end of the range to return a value from, inclusive
     * @returns {int} Pseudorandom value [lowerBound, upperBound], using ROT.RNG.getUniform() to distribute the value
     */
    fun getUniformInt(lowerBound : Int, upperBound: Int) : Int {
        val max = Math.max(lowerBound, upperBound)
        val min = Math.min(lowerBound, upperBound)
        return (Math.floor(getUniform() * (max - min + 1)) + min).toInt()
    }

    /**
     * @param {float} [mean=0] Mean value
     * @param {float} [stddev=1] Standard deviation. ~95% of the absolute values will be lower than 2*stddev.
     * @returns {float} A normally distributed pseudorandom value
     */
    fun getNormal(mean : Double = 0.0, stddev : Double = 1.0) : Double {
        var u : Double
        var v : Double
        var r : Double
        do {
            u = 2*this.getUniform()-1
            v = 2*this.getUniform()-1
            r = u*u + v*v
        } while (r > 1 || r == 0.0)

        val gauss = u * Math.sqrt(-2 * Math.log(r) / r)
        return mean + gauss * stddev
    }

    /**
     * @returns {int} Pseudorandom value [1,100] inclusive, uniformly distributed
     */
    fun getPercentage() = 1 + Math.floor(this.getUniform()*100).toInt()

    /**
     * @param {object} data key=whatever, value=weight (relative probability)
     * @returns {string} whatever
     */
    fun getWeightedValue(data : Map<String, Double?>) : String {
        val total = data.keys.sumByDouble { data[it]!! }

        val random = this.getUniform()*total

        var part = 0.0
        var lid : String = ""
        data.keys.forEach { id ->
            part += data[id]!!
            if (random < part) { return id }
            lid = id
        }

        // If by some floating-point annoyance we have
        // random >= total, just return the last id.
        return lid
    }

    /**
     * Get RNG state. Useful for storing the state and re-setting it via setState.
     * @returns {?} Internal state
     */
    fun getState() = RNGState(s0, s1, s2, c)

    /**
     * Set a previously retrieved state.
     * @param {?} state
     */
    fun setState(state : RNGState) : Unit {
        s0 = state.s0
        s1 = state.s1
        s2 = state.s2
        c  = state.c
    }

    fun copy(ns0: Double = s0, ns1: Double = s1, ns2: Double = s2, nc : Double = c) : RNGType = RNGType(ns0, ns1, ns2, nc)

    /**
     * Returns a cloned RNG
     */
    fun clone() = copy()
}

object RNG : RNGType() {

    init {
        seed = System.currentTimeMillis().toDouble()
    }
}

