/**
 * Created by ejw on 5/28/17.
 */

package us.cornus.rotlin

import org.junit.Assert.*
import org.junit.Test


class RNGTest {

    companion object {
        val EPSILON = 0.0
    }

    @Test fun getUniformTest() {
        val value = RNG.getUniform()
        assertTrue("should return a number", value is Double)
        assertTrue("should return a number 0..1", value > 0)
        assertTrue("should return a number 0..1", value < 1)
        }

    @Test fun getUniformIntTest() {
        val lowerBound = 5
        val upperBound = 10

        val value0 = RNG.getUniformInt(lowerBound, upperBound)
        assertTrue("should return a number", value0 is Int)

        val seed = Math.round(Math.random() * 1000000).toDouble()
        RNG.seed = seed
        val value1 = RNG.getUniformInt(lowerBound, upperBound)
        RNG.seed = seed
        val value2 = RNG.getUniformInt(upperBound, lowerBound)
        assertEquals("should not care which number is larger in the arguments", value1, value2)

        val value3 = RNG.getUniformInt(lowerBound, upperBound)
        val value4 = RNG.getUniformInt(upperBound, lowerBound)
        val msg = "should only return a number in the desired range"
        assertFalse(msg, value3 > upperBound)
        assertFalse(msg, value3 < lowerBound)
        assertFalse(msg, value4 > upperBound)
        assertFalse(msg, value4 < lowerBound)
    }

    @Test fun seedingTest() {
        assertTrue("should return a seed number", RNG.seed is Double)

        val seed = Math.round(Math.random()*1000000).toDouble()
        RNG.seed = seed
        val val1 =RNG.getUniform()
        RNG.seed = seed
        val val2 = RNG.getUniform()
        assertEquals("should return the same value for a given seed", val1, val2, EPSILON)

        RNG.seed = 12345.0
        val value = RNG.getUniform()
        assertEquals("should return a precomputed value for a given seed", 0.01198604702949524, value, EPSILON)
    }

    @Test fun stateManipulationTest() {
        RNG.getUniform() //Discard value intentionally

        val state = RNG.getState()
        val val1 = RNG.getUniform()
        RNG.setState(state)
        val val2 = RNG.getUniform()
        assertEquals("should return identical values after setting identical states", val1, val2, EPSILON)
    }

    @Test fun cloningTest() {
        val clone0 = RNG.clone()
        assertTrue("should be able to clone an RNG", clone0 is RNGType)

        val clone1 = RNG.clone()
        val num = clone1.getUniform()
        assertTrue("should clone a working RNG", num is Double)

        val clone3 = RNG.clone()
        val num1 = RNG.getUniform()
        val num2 = clone3.getUniform()
        assertEquals("should clone maintaining its state", num1, num2, EPSILON)
    }

    //This is not a good test. It doesn't actually test anything. It is a stub for where the test should be.
    @Test fun getNormalTest() {
        val value = RNG.getNormal()
        assertTrue("should return a number", value is Double)
//        assertTrue("should return a number 0..1", value > 0)
//        assertTrue("should return a number 0..1", value < 1)
    }

    @Test fun getPercentageTest() {
        val value = RNG.getPercentage()
        assert(value >= 1)
        assert(value <= 100)
    }
}