package net.barribob.maelstrom.general

class TimedEvent(
        val shouldCancel: () -> Boolean,
        val callback: () -> Unit,
        val ticks: Int,
        val duration: Int,
        val getCurrentTicks: () -> Int) : IEvent {

    override fun shouldDoEvent(): Boolean = this.ticks <= getCurrentTicks()

    override fun doEvent() = callback()

    override fun shouldRemoveEvent(): Boolean = shouldCancel() || this.ticks + duration <= getCurrentTicks()

    override fun tickSize(): Int = 1
}