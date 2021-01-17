package general.event
import net.barribob.maelstrom.general.event.EventScheduler
import net.barribob.maelstrom.general.event.TimedEvent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestTimedEvent {

    @Test
    fun eventAddedAndFired() {
        val eventManager = EventScheduler()
        var eventsFired = 0
        val incrementer = { eventsFired += 1 }

        eventManager.addEvent(TimedEvent(incrementer, 1))
        eventManager.updateEvents()
        assertEquals(0, eventsFired)
        eventManager.updateEvents()
        assertEquals(1, eventsFired)
    }

    @Test
    fun multipleEventsFired() {
        val eventManager = EventScheduler()
        var eventsFired = 0
        val incrementer = { eventsFired += 1 }

        eventManager.addEvent(TimedEvent(incrementer, 1))
        eventManager.updateEvents()
        eventManager.addEvent(TimedEvent(incrementer, 3))
        eventManager.updateEvents()
        assertEquals(1, eventsFired)

        eventManager.addEvent(TimedEvent(incrementer, 1))
        eventManager.updateEvents()
        assertEquals(1, eventsFired)
        eventManager.updateEvents()
        assertEquals(2, eventsFired)
        eventManager.updateEvents()
        assertEquals(3, eventsFired)
    }

    @Test
    fun orderOfEventsFired() {
        val eventManager = EventScheduler()
        var eventStr = ""

        eventManager.addEvent(TimedEvent({ eventStr += "Fourth!" }, 3))
        eventManager.addEvent(TimedEvent({ eventStr += "Third!" }, 2))
        eventManager.addEvent(TimedEvent({ eventStr += "First!" }, 1))
        eventManager.addEvent(TimedEvent({ eventStr += "Second!" }, 1))

        eventManager.updateEvents()
        eventManager.updateEvents()
        assertEquals("First!Second!", eventStr)
        eventManager.updateEvents()
        assertEquals("First!Second!Third!", eventStr)
        eventManager.updateEvents()
        assertEquals("First!Second!Third!Fourth!", eventStr)
    }

    @Test
    fun eventCancel() {
        val eventManager = EventScheduler()
        var eventStr = ""

        eventManager.addEvent(TimedEvent({ eventStr += "Should not be assigned!" }, 0, shouldCancel = { true }))
        eventManager.updateEvents()
        eventManager.updateEvents()
        assertEquals("", eventStr)
    }

    @Test
    fun duration() {
        val eventManager = EventScheduler()
        var eventsFired = 0
        val incrementer = { eventsFired += 1 }

        eventManager.addEvent(TimedEvent(incrementer, 1, 3))
        for(i in 0..5) {
            eventManager.updateEvents()
        }
        assertEquals(3, eventsFired)
    }

    @Test
    fun eventMidwayCancel() {
        val eventManager = EventScheduler()
        var timesRun = 0
        val cancelAfterThreeRuns = { timesRun == 3 }

        eventManager.addEvent(TimedEvent({ timesRun++ }, 0, 10, cancelAfterThreeRuns))
        for(i in 0..10) {
            eventManager.updateEvents()
        }
        assertEquals(3, timesRun)
    }
}