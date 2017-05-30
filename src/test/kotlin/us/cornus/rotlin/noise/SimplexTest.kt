package us.cornus.rotlin.noise

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by ejw on 5/30/17.
 */
class SimplexTest {
        val EPSILON = 0.0

   @Test
    fun get() {
        val simplex = Simplex()
        val noise0 = simplex.get(0, 0)
        assertEquals(0.0, noise0, EPSILON)
    }
}