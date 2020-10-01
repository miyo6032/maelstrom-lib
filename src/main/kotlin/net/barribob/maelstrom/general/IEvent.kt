package net.barribob.maelstrom.general

interface IEvent {
    fun shouldDoEvent(): Boolean
    fun doEvent()
    fun shouldRemoveEvent(): Boolean
    fun tickSize(): Int
}