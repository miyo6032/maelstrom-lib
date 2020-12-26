package net.barribob.maelstrom.general.data

class HistoricalData <T> (defaultValue: T, private val maxHistory: Int = 2) {
    private val history = mutableListOf(defaultValue)

    init {
        if (maxHistory < 2) throw IllegalArgumentException("Max History cannot be less than 2")
    }

    fun set(value: T) {
        history.add(value)
        if (history.size > maxHistory) {
            history.removeAt(0)
        }
    }

    fun get(past: Int = 0): T {
        if (past < 0) throw IllegalArgumentException("Past cannot be negative")

        val clampedPast = (history.size - 1 - past).coerceAtLeast(0)
        return history[clampedPast]
    }

    fun getAll() = history.toList()
}