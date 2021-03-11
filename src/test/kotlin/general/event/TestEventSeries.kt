package general.event

import net.barribob.maelstrom.general.event.Event
import net.barribob.maelstrom.general.event.EventScheduler
import net.barribob.maelstrom.general.event.EventSeries
import net.barribob.maelstrom.general.event.TimedEvent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestEventSeries {
    @Test
    fun eventsFireInSequence() {
        val eventScheduler = EventScheduler()
        var eventFired = false
        val event = TimedEvent({}, 4)
        val secondEvent = TimedEvent({ eventFired = true }, 4)

        eventScheduler.addEvent(EventSeries(event, secondEvent))

        for (i in 0 until 10) {
            eventScheduler.updateEvents()
        }

        Assertions.assertTrue(eventFired)
    }

    @Test
    fun seriesSkipsCanceledEvent() {
        val eventScheduler = EventScheduler()
        var eventFired = false
        var canceledEventFired = false
        val event = TimedEvent({}, 0)
        val canceledEvent = Event({ true }, { canceledEventFired = true }, { true })
        val thirdEvent = TimedEvent({ eventFired = true }, 0)

        eventScheduler.addEvent(EventSeries(event, canceledEvent, thirdEvent))

        for (i in 0 until 2) {
            eventScheduler.updateEvents()
        }

        Assertions.assertAll(
            { Assertions.assertFalse(canceledEventFired) },
            { Assertions.assertTrue(eventFired) }
        )
    }

    @Test
    fun seriesThrowsErrorWhenNoEventsDefined() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { EventSeries() }
    }
}