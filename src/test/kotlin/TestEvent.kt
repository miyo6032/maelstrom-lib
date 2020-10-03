import net.barribob.maelstrom.general.Event
import net.barribob.maelstrom.general.EventScheduler
import net.barribob.maelstrom.general.TimedEvent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

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