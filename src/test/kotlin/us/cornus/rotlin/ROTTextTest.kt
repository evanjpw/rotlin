package us.cornus.rotlin

/**
 * Created by ejw on 5/29/17.
 */

import org.junit.Assert.*
import org.junit.Test

class ROTTextTest {

    companion object {
        val A100 = "A".repeat(101)
        val B100 = "B".repeat(101)
    }

    @Test fun lineBreakingTest() {
        val size0 = ROTText.measure(A100)
        assertEquals( A100.length, size0.width)
        assertEquals("should not break when not requested", 1, size0.height)

        val size1 = ROTText.measure(A100, 30)
        assertEquals("should break when max length requested", 4, size1.height)

        val size3 = ROTText.measure("a\nb\nc")
        assertEquals("should break at explicit newlines", 3, size3.height)

        val size2 = ROTText.measure(A100 + B100, 30)
        assertEquals(7, size2.height)

        val size4 = ROTText.measure(A100 + "\n" + B100, 30)
        assertEquals("should break at explicit newlines AND max length", 8, size4.height)

        val size5 = ROTText.measure(A100 + " " + B100, 30)
        assertEquals("should break at space", 8, size5.height)

        val size6 = ROTText.measure(A100 + 160.toChar() + B100, 30)
        assertEquals("should not break at nbsp", 7, size6.height)

        val size7 = ROTText.measure("aaa bbb", 7)
        assertEquals(7, size7.width)
        assertEquals("should not break when text is short", 1, size7.height)

        val size8 = ROTText.measure("aaa bbb", 6)
        assertEquals(3, size8.width)
        assertEquals("should adjust resulting width", 2, size8.height)

        val size9 = ROTText.measure("aaa ", 6)
        assertEquals(3, size9.width)
        assertEquals("should adjust resulting width even without breaks", 1, size9.height)

        val size10 = ROTText.measure("aaa  \n  bbb")
        assertEquals(3, size10.width)
        assertEquals("should remove unnecessary spaces around newlines", 2, size10.height)

        val size11 = ROTText.measure("   aaa    bbb", 3)
        assertEquals(3, size11.width)
        assertEquals("should remove unnecessary spaces at the beginning", 2, size11.height)

        val size12 = ROTText.measure("aaa    \nbbb", 3)
        assertEquals(3, size12.width)
        assertEquals("should remove unnecessary spaces at the end", 2, size12.height)
    }

    @Test fun colorFormatting() {
        val size0 = ROTText.measure("aaa%c{x}bbb")
        assertEquals("should not break with formatting part", 1, size0.height)

        val size1 = ROTText.measure("aaa%c{x}bbb")
        assertEquals("should correctly remove formatting", 6, size1.width)

        val size2 = ROTText.measure("aaa%c{x}bbb", 3)
        assertEquals(3, size2.width)
        assertEquals("should break independently on formatting - forced break", 2, size2.height)

        val size3 = ROTText.measure("aaa%c{x}b bb", 5)
        assertEquals(4, size3.width)
        assertEquals("should break independently on formatting - forward break", 2, size3.height)

        val size4 = ROTText.measure("aa a%c{x}bbb", 5)
        assertEquals(4, size4.width)
        assertEquals("should break independently on formatting - backward break", 2, size4.height)
    }
}