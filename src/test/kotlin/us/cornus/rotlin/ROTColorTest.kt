package us.cornus.rotlin

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by ejw on 6/1/17.
 */
class ROTColorTest {

    private val EPSILON = 0.01f

    @Test
    fun fromStringTest() {
        assertArrayEquals("should handle rgb() colors", intArrayOf(10, 20, 33), ROTColor.fromString("rgb(10, 20, 33)").toIntArray())

        assertArrayEquals("should handle #abcdef colors", intArrayOf(26, 47, 60), ROTColor.fromString("#1a2f3c").toIntArray())

        assertArrayEquals("should handle #abc colors", intArrayOf(204, 170, 136), ROTColor.fromString("#ca8").toIntArray())

        assertArrayEquals("should handle named colors", intArrayOf(255, 0, 0), ROTColor.fromString("red").toIntArray())

        assertArrayEquals("should not handle nonexistent colors", intArrayOf(0, 0, 0), ROTColor.fromString("lol").toIntArray())
    }

    @Test
    fun addTest() {
        assertArrayEquals("should add two colors", intArrayOf(4,6,8), ROTColor.add(listOf(1,2,3), listOf(3,4,5)).toIntArray())

        assertArrayEquals("should add three colors", intArrayOf(104,206,308), ROTColor.add(listOf(1,2,3), listOf(3,4,5), listOf(100,200,300)).toIntArray())

        assertArrayEquals("should add one color (noop)", intArrayOf(1,2,3), ROTColor.add(listOf(1,2,3)).toIntArray())

        val c1 = listOf(1,2,3)
        val c2 = listOf(3,4,5)
        ROTColor.add(c1, c2)
        assertEquals("should not modify first argument values", listOf(1,2,3), c1)
    }

    @Test
    fun add_Test() {
        val v1 = mutableListOf(1,2,3)
        assertArrayEquals("should add two colors", intArrayOf(4,6,8), ROTColor.add_(v1, listOf(3,4,5)).toIntArray())

        val v2 = mutableListOf(1,2,3)
        assertArrayEquals("should add three colors", intArrayOf(104,206,308), ROTColor.add_(v2, listOf(3,4,5), listOf(100,200,300)).toIntArray())

        val v3 = mutableListOf(1,2,3)
        assertArrayEquals("should add one color (noop)", intArrayOf(1,2,3), ROTColor.add_(v3).toIntArray())

        val c1 = mutableListOf(1,2,3)
        val c2 = listOf(3,4,5)
        ROTColor.add_(c1, c2)
        assertArrayEquals("should modify first argument values", intArrayOf(4,6,8), c1.toIntArray())

        val c3 = mutableListOf(1,2,3)
        val c4 = listOf(3,4,5)
        val c5 = ROTColor.add_(c3, c4)
        assertArrayEquals("should return first argument", c3.toIntArray(), c5.toIntArray())
    }

    @Test
    fun multiplyTest() {
            assertArrayEquals("should multiply two colors", intArrayOf(20,40,60), ROTColor.multiply(listOf(100,200,300), listOf(51,51,51)).toIntArray())

            assertArrayEquals("should multiply three colors", intArrayOf(40,80,120), ROTColor.multiply(listOf(100,200,300), listOf(51,51,51), listOf(510,510,510)).toIntArray())

            assertArrayEquals("should multiply one color (noop)", intArrayOf(1,2,3), ROTColor.multiply(listOf(1,2,3)).toIntArray())

        val c1 = listOf(1,2,3)
        val c2 = listOf(3,4,5)
        ROTColor.multiply(c1, c2)
        assertArrayEquals("should not modify first argument values", intArrayOf(1,2,3), c1.toIntArray())

        assertArrayEquals("should round values", intArrayOf(4,8,12), ROTColor.multiply(listOf(100,200,300), listOf(10, 10, 10)).toIntArray())
    }

    @Test
    fun multiply_Test() {
        val v1 = mutableListOf(100,200,300)
        assertArrayEquals("should multiply two colors", intArrayOf(20,40,60), ROTColor.multiply_(v1, listOf(51,51,51)).toIntArray())

        val v2 = mutableListOf(100,200,300)
        assertArrayEquals("should multiply three colors", intArrayOf(40,80,120), ROTColor.multiply_(v2, listOf(51,51,51), listOf(510,510,510)).toIntArray())

        val v3 = mutableListOf(1,2,3)
        assertArrayEquals("should multiply one color (noop)", intArrayOf(1,2,3), ROTColor.multiply_(v3).toIntArray())

        val c1 = mutableListOf(100,200,300)
        val c2 = listOf(51,51,51)
        ROTColor.multiply_(c1, c2)
        assertArrayEquals("should modify first argument values", intArrayOf(20,40,60), c1.toIntArray())

        val v4 = mutableListOf(100,200,300)
        assertArrayEquals("should round values", intArrayOf(4,8,12), ROTColor.multiply_(v4, listOf(10, 10, 10)).toIntArray())

        val c10 = mutableListOf(1,2,3)
        val c20 = listOf(3,4,5)
        val c30 = ROTColor.multiply_(c10, c20)
        assertArrayEquals("should return first argument", c30.toIntArray(), c10.toIntArray())
    }

    @Test
    fun interpolateTest() {
        assertArrayEquals("should interpolate two colors", intArrayOf(19, 38, 66), ROTColor.interpolate(listOf(10, 20, 40), listOf(100, 200, 300), 0.1f).toIntArray())

        assertArrayEquals("should round values", intArrayOf(13, 25, 47), ROTColor.interpolate(listOf(10, 20, 40), listOf(15, 30, 53), 0.5f).toIntArray())

        assertArrayEquals("should default to 0.5 factor", intArrayOf(15, 25, 40), ROTColor.interpolate(listOf(10, 20, 40), listOf(20, 30, 40)).toIntArray())
    }

    @Test
    fun interpolateHSLTest() {
        assertArrayEquals("should intepolate two colors", intArrayOf(12, 33, 73), ROTColor.interpolateHSL(listOf(10, 20, 40), listOf(100, 200, 300), 0.1f).toIntArray())
    }

    @Test
    fun rgb2hslTest() {
        val rgb = arrayListOf(
        listOf(255, 255, 255),
        listOf(0, 0, 0),
        listOf(255, 0, 0),
        listOf(30, 30, 30),
        listOf(100, 120, 140)
        )

        while (rgb.isNotEmpty()) {
            val color: List<Int> = rgb.pop()!!
            val hsl = ROTColor.rgb2hsl(color)
            val rgb2 = ROTColor.hsl2rgb(hsl)
            assertArrayEquals("should correctly convert to HSL and back", color.toIntArray(), rgb2.toIntArray())
        }
    }

    @Test
    fun hsl2rgbTest() {
        val hsl = listOf(0.5f, 0.3f, 0.3f)
        val rgb = ROTColor.hsl2rgb(hsl)
        val hsl2 = ROTColor.rgb2hsl(rgb).toList()
        for (i in hsl.indices) {
            assertEquals("should round converted values", hsl[i], hsl2[i], EPSILON)
        }
    }

    @Test
    fun toRGBTest() {
        assertEquals("should serialize to rgb", "rgb(10,20,30)", ROTColor.toRGB(listOf(10, 20, 30)))

assertEquals("should clamp values to 0..255", "rgb(0,20,255)", ROTColor.toRGB(listOf(-100, 20, 2000)))
    }

    @Test
    fun toHexTest() {
        assertEquals("should serialize to hex", "#0a1428", ROTColor.toHex(listOf(10, 20, 40)))

        assertEquals("should clamp values to 0..255", "#0014ff", ROTColor.toHex(listOf(-100, 20, 2000)))
    }

    @Test
    fun randomizeTest() {
        val c: IntArray = ROTColor.randomize(listOf(100, 100, 100), 100.0) .toIntArray()
        assertEquals(c[1], c[0])
        assertEquals("should maintain constant diff when a number is used", c[2], c[1])
    }
}