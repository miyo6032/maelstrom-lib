package net.barribob.maelstrom.static_utilities

import kotlin.random.Random

object RandomUtils {
    private val rand = Random

    /**
     * Creates a random value between -range and range
     */
    // TODO: Test
    fun double(range: Double): Double {
        return (rand.nextDouble() - 0.5) * 2 * range
    }
}
