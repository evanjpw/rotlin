package us.cornus.rotlin

/**
 * Created by ejw on 5/29/17.
 */

/**
 * A wrapper class for events.
 * The original motivations for this were:
 * 1. Enforce some safety in enqueuing (so that elementary mistakes are caught
 * 2. Provide a way of distinguishing the two 'remove' methods
 * 3. Provide a defined type so that changes and refactors catch erroneous code
 *
 * Subsequently I realized that the second 'remove' method was not supposed to be
 * exposed in the public API of the class (so #2 is not so valid), but I believe the
 * decision to be sound anyway, and I am keeping things as they are.
 */
data class Event(val thing : Any)

/**
 * @class Generic event queue: stores events and retrieves them based on their time
 */
class EventQueue {
    private val events : ArrayList<Event> = ArrayList()
    private val eventTimes : ArrayList<Long> = ArrayList()

    /**
     * @returns {number} Elapsed time
     */
    var time : Long = 0
        get() = field
        private set(value) {
            field = value
        }

    /**
     * Clear all scheduled events
     */
    fun clear(): EventQueue {
        events.clear()
        eventTimes.clear()
        return this
    }

    /**
     * @param {?} event
     * @param {number} time
     */
    fun add(newEvent: Event, newTime: Long) {
        var index = events.size
        for (i in eventTimes.indices) {
            if (eventTimes[i] > newTime) {
                index = i
                break
            }
        }

        events.add(index, newEvent)
        eventTimes.add(index, newTime)
    }

    /**
     * Locates the nearest event, advances time if necessary. Returns that event and removes it from the queue.
     * @returns {? || null} The event previously added by addEvent, null if no event available
     */
    fun get() : Event? {
        if (events.isEmpty()) { return null }

        val xtime = eventTimes.pop() ?: 0
        if (xtime > 0) { /* advance */
            time += xtime
            eventTimes.indices.forEach { eventTimes[it] -= xtime }
        }

        return events.pop()
    }

    /**
     * Get the time associated with the given event
     * @param {?} event
     * @returns {number} time
     */
    fun getEventTime(event : Event) : Long? {
        val index = events.indexOf(event)
        return if (index == -1) null else eventTimes[index]
    }

    /**
     * Remove an event from the queue
     * @param {?} event
     * @returns {bool} success?
     */
    fun remove(event : Event) : Boolean {
        val index = events.indexOf(event)
        if (index ==  -1) return false
        remove(index)
        return true
    }

    /**
     * Remove an event from the queue
     * @param {int} index
     */
    fun remove(index : Int) : Unit {
        events.removeAt(index)
        eventTimes.removeAt(index)
    }
}