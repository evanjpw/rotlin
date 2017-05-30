package us.cornus.rotlin.noise

import us.cornus.rotlin.push
import us.cornus.rotlin.randomize
import us.cornus.rotlin.rotmod
/**
 * Created by ejw on 5/30/17.
 */

/**
 * A simple 2d implementation of simplex noise by Ondrej Zara
 *
 * Based on a speed-improved simplex noise algorithm for 2D, 3D and 4D in Java.
 * Which is based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * With Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 */

/**
 * @class 2D simplex noise generator
 * @param {int} [gradients=256] Random gradients
 */
class Simplex(gradientsCount : Int = 256) : Noise() {
    private val F2 = 0.5 * (Math.sqrt(3.0) - 1)
    private val G2 = (3 - Math.sqrt(3.0)) / 6

    private val gradients: List<List<Int>> = listOf(
            listOf(0, -1),
            listOf(1, -1),
            listOf(1,  0),
            listOf(1,  1),
            listOf(0,  1),
            listOf(-1,  1),
            listOf(-1,  0),
            listOf(-1, -1)
    )

    private val perms = ArrayList<Int>()
    private val indexes = ArrayList<Int>()

    init {

        var permutations : ArrayList<Int> = ArrayList()
        val count = gradientsCount
        for (i in 0..count) permutations.push(i)
        permutations = permutations.randomize()

        for (i in 0..count) {
            perms.push(permutations[i % count])
            indexes.push(perms[i] % gradients.size)
            //perms[i] % gradients.size)
        }
    }

    override fun get(x: Double, y: Double) : Double {
        val perms = perms
        val indexes = indexes
        val count: Int = perms.size/2
        val G2 = G2

        var n0 = 0.0
        var n1 = 0.0
        var n2 = 0.0
        var gi : Int // Noise contributions from the three corners

        // Skew the input space to determine which simplex cell we're in
        val s = (x + y) * F2 // Hairy factor for 2D
        val i = Math.floor(x + s).toInt()
        val j = Math.floor(y + s).toInt()
        val t = (i + j) * G2
        val X0 = i - t // Unskew the cell origin back to (x,y) space
        val Y0 = j - t
        val x0 = x - X0 // The x,y distances from the cell origin
        val y0 = y - Y0

        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        val i1 : Int
        val j1 : Int // Offsets for second (middle) corner of simplex in (i,j) coords
        if (x0 > y0) {
            i1 = 1
            j1 = 0
        } else { // lower triangle, XY order: (0,0)->(1,0)->(1,1)
            i1 = 0
            j1 = 1
        } // upper triangle, YX order: (0,0)->(0,1)->(1,1)

        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        val x1 = x0 - i1 + G2 // Offsets for middle corner in (x,y) unskewed coords
        val y1 = y0 - j1 + G2
        val x2 = x0 - 1 + 2*G2 // Offsets for last corner in (x,y) unskewed coords
        val y2 = y0 - 1 + 2*G2

        // Work out the hashed gradient indices of the three simplex corners
        val ii = i.rotmod(count)
        val jj = j.rotmod(count)

        // Calculate the contribution from the three corners
        var t0 = 0.5 - x0*x0 - y0*y0
        if (t0 >= 0) {
            t0 *= t0
            gi = indexes[ii+perms[jj]]
            val grad = gradients[gi]
            n0 = t0 * t0 * (grad[0] * x0 + grad[1] * y0)
        }

        var t1 = 0.5 - x1*x1 - y1*y1
        if (t1 >= 0) {
            t1 *= t1
            gi = indexes[ii+i1+perms[jj+j1]]
            val grad = gradients[gi]
            n1 = t1 * t1 * (grad[0] * x1 + grad[1] * y1)
        }

        var t2 = 0.5 - x2*x2 - y2*y2
        if (t2 >= 0) {
            t2 *= t2
            gi = indexes[ii+1+perms[jj+1]]
            val grad = gradients[gi]
            n2 = t2 * t2 * (grad[0] * x2 + grad[1] * y2)
        }

        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70 * (n0 + n1 + n2)
    }
    /*

      */
}