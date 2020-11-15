package net.barribob.maelstrom.static_utilities

import kotlin.random.Random

object RandomUtils {
    private val rand = Random

    /**
     * Creates a random value between -range and range
     */
    fun double(range: Double): Double {
        return (rand.nextDouble() - 0.5) * 2 * range
    }

    /**
     * Chooses a random integer between the min (inclusive) and the max (exclusive)
     *
     * @param min
     * @param max
     * @return
     */
    fun range(min: Int, max: Int): Int {
        require(min <= max) { "Minimum is greater than maximum" }
        val range = max - min
        return min + rand.nextInt(range)
    }
}
