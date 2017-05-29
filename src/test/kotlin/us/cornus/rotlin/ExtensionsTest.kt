package us.cornus.rotlin

/**
 * Created by ejw on 5/28/17.
 */

import org.junit.Assert.*
import org.junit.Test

class ExtensionsTest {
    @Test fun lpadTest() {
        assertEquals("should lpad with defaults", "0a", "a".lpad())
        assertEquals("should lpad with char", "ba", "a".lpad('b'))
        assertEquals("should lpad with count", "bba", "a".lpad('b', 3))
        assertEquals("should not lpad when not necessary", "aaa", "aaa".lpad('b', 3))
    }

    @Test fun rpadTest() {
        assertEquals("should rpad with defaults", "a0", "a".rpad())
        assertEquals("should rpad with char", "ab", "a".rpad('b'))
        assertEquals("should rpad with count", "abb", "a".rpad('b', 3))
        assertEquals("should not rpad when not necessary", "aaa", "aaa".rpad('b', 3))
    }

    @Test fun rotmodTest() {
        assertEquals("should compute modulus of a positive number", 1, (7).rotmod(3))
        assertEquals("should compute modulus of a negative number", 2, (-7).rotmod(3))
    }
}
