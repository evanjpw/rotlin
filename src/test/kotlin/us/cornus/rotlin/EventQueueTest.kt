package us.cornus.rotlin

import org.junit.Assert.*
import org.junit.Test

/**
 * Created by ejw on 5/29/17.
 */
class EventQueueTest {
    @Test
    fun getTimeTest() {
        val q = EventQueue()
        q.add(Event(456), 10)
        q.add(Event(123), 5)
        q.add(Event(789), 15)
        q.get() //Discard
        q.get() //Discard
        q.get() //Discard
        val time = q.time
        assertEquals("should compute elapsed time", 15L, time)
    }

    @Test
    fun clearTest() {
        val q = EventQueue()
        q.add(Event(0), 0)
        q.clear()
        assertNull("should clear all events", q.get())
    }

    @Test
    fun addTest() {
        val q = EventQueue()
        q.add(Event("a"), 100)
        val gotQ: Event? = q.get()
        assertNotNull("should return added event", gotQ)
        assertEquals("a", gotQ?.thing)
    }

    @Test
    fun getTest() {
        val q = EventQueue()
        assertNull("should return null when no events are available", q.get())
    }

    @Test
    fun get2Test() {
        val q = EventQueue()
        q.add(Event(0), 0)
        q.get() //Discard
        assertNull("should remove returned events", q.get())
    }

    @Test
    fun getEventTimeTest() {
        val q = EventQueue()
        q.add(Event(123), 187)
        q.add(Event(456), 42)
        val evTime1 = q.getEventTime(Event(123))
        val evTime2 = q.getEventTime(Event(456))
        assertNotNull(evTime1)
        assertNotNull(evTime2)
        assertEquals(187L, evTime1)
        assertEquals("should look up time of events", 42L, evTime2)
    }

    @Test
    fun getEventTime2Test() {
        val q = EventQueue()
        q.add(Event(123), 187)
        q.add(Event(456), 42)
        q.add(Event(789), 411)
        q.get()
        val evTime1 = q.getEventTime(Event(456))
        val evTime2 = q.getEventTime(Event(123))
        val evTime3 = q.getEventTime(Event(789))
        assertNull(evTime1)
        assertNotNull(evTime2)
        assertNotNull(evTime3)
        assertEquals(187L - 42L, evTime2)
        assertEquals("should look up correct times after events removed", 411L - 42L, evTime3)
    }

    @Test
    fun removeTest() {
        val q = EventQueue()
        q.add(Event(123), 0)
        q.add(Event(456), 0)
        val result = q.remove(Event(123))
        assertTrue(result)
        val gotQ = q.get()
        assertNotNull(gotQ)
        assertEquals("should remove events", 456, gotQ?.thing)
    }

    @Test
    fun remove1Test() {
        val q = EventQueue()
        q.add(Event(0), 0)
        val result = q.remove(Event(1))
        assertFalse("should survive removal of non-existant events", result)
        val gotQ = q.get()
        assertEquals(0, gotQ?.thing)
    }

    @Test
    fun removeByIndexTest() {
        val q = EventQueue()
        q.add(Event(123), 0)
        q.add(Event(456), 0)
        q.remove(0)
        val gotQ = q.get()
        assertNotNull(gotQ)
        assertEquals("should remove events by index", 456, gotQ?.thing)
    }

    @Test
    fun eventSortingTest() {
        val q = EventQueue()
        q.add(Event(456), 10)
        q.add(Event(123), 5)
        q.add(Event(789), 15)
        val gotQ1 = q.get()
        val gotQ2 = q.get()
        val gotQ3 = q.get()
        assertNotNull(gotQ1)
        assertNotNull(gotQ2)
        assertNotNull(gotQ3)
        assertEquals(123, gotQ1?.thing)
        assertEquals(456, gotQ2?.thing)
        assertEquals("should return events sorted", 789, gotQ3?.thing)
    }

    @Test
    fun timestampOrderTest() {
        val q = EventQueue()
        q.add(Event(456), 10)
        q.add(Event(123), 10)
        q.add(Event(789), 10)
        val gotQ1 = q.get()
        val gotQ2 = q.get()
        val gotQ3 = q.get()
        assertNotNull(gotQ1)
        assertNotNull(gotQ2)
        assertNotNull(gotQ3)
        assertEquals(456, gotQ1?.thing)
        assertEquals(123, gotQ2?.thing)
        assertEquals(789, gotQ3?.thing)
        val time = q.time
        assertEquals("should maintain event order for same timestamps", 10L, time)
    }
}