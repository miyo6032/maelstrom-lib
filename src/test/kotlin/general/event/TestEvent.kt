package general.event

import net.barribob.maelstrom.general.event.Event
import net.barribob.maelstrom.general.event.EventScheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestEvent {

    @Test
    fun conditionalFires() {
        val eventScheduler = EventScheduler()
        var ticks = 0
        val condition = { ticks == 10 }
        var eventFired = true
        val callback = { eventFired = true }
        val event = Event(condition, callback)

        eventScheduler.addEvent(event)
        for(i in 0..10) {
            eventScheduler.updateEvents()
            ticks++
        }

        assertEquals(true, eventFired)
    }

    @Test
    fun eventCancel() {
        val eventManager = EventScheduler()
        var eventStr = ""
        var ticks = 0

        eventManager.addEvent(Event({ ticks == 1 }, { eventStr += "Should not be assigned!" }, shouldCancel = { true }))
        eventManager.updateEvents()
        ticks++
        eventManager.updateEvents()

        assertEquals("", eventStr)
    }
}