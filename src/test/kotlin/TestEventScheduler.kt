import net.barribob.maelstrom.general.EventScheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestEventScheduler {

    @Test
    fun testEventAddedAndFired() {
        val eventManager = EventScheduler()
        var eventsFired = 0
        val incrementer = { eventsFired += 1 }

        eventManager.addTimedEvent({ false }, incrementer, 1)
        eventManager.updateEvents()
        assertEquals(0, eventsFired)
        eventManager.updateEvents()
        assertEquals(1, eventsFired)
    }

    @Test
    fun testMultipleEventsFired() {
        val eventManager = EventScheduler()
        var eventsFired = 0
        val incrementer = { eventsFired += 1 }

        eventManager.addTimedEvent({ false }, incrementer, 1)
        eventManager.updateEvents()
        eventManager.addTimedEvent({ false }, incrementer, 3)
        eventManager.updateEvents()
        assertEquals(1, eventsFired)

        eventManager.addTimedEvent({ false }, incrementer, 1)
        eventManager.updateEvents()
        assertEquals(1, eventsFired)
        eventManager.updateEvents()
        assertEquals(2, eventsFired)
        eventManager.updateEvents()
        assertEquals(3, eventsFired)
    }

    @Test
    fun testOrderOfEventsFired() {
        val eventManager = EventScheduler()
        var eventStr = ""

        eventManager.addTimedEvent({ false }, { eventStr += "Fourth!" }, 3)
        eventManager.addTimedEvent({ false }, { eventStr += "Third!" }, 2)
        eventManager.addTimedEvent({ false }, { eventStr += "First!" }, 1)
        eventManager.addTimedEvent({ false }, { eventStr += "Second!" }, 1)

        eventManager.updateEvents()
        eventManager.updateEvents()
        assertEquals("First!Second!", eventStr)
        eventManager.updateEvents()
        assertEquals("First!Second!Third!", eventStr)
        eventManager.updateEvents()
        assertEquals("First!Second!Third!Fourth!", eventStr)
    }

    @Test
    fun testEventCancel() {
        val eventManager = EventScheduler()
        var eventStr = ""

        eventManager.addTimedEvent({ true }, { eventStr += "Should not be assigned!" }, 1)
        eventManager.updateEvents()
        eventManager.updateEvents()
        assertEquals("", eventStr)
    }

    @Test
    fun testDuration() {
        val eventManager = EventScheduler()
        var eventsFired = 0
        val incrementer = { eventsFired += 1 }

        eventManager.addTimedEvent({ false }, incrementer, 1, 2)
        for(i in 0..5) {
            eventManager.updateEvents()
        }
        assertEquals(3, eventsFired)
    }
}