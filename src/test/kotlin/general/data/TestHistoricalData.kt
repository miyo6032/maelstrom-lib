package general.data

import net.barribob.maelstrom.general.data.HistoricalData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestHistoricalData {
    @Test
    fun whenInitialized_ProvidesDefaultValue() {
        val defaultValue = 0
        val data = createHistoricalData(defaultValue)

        Assertions.assertEquals(defaultValue, data.get())
    }

    @Test
    fun whenGivenNegativePast_ThrowsError() {
        val data = createHistoricalData(0)

        Assertions.assertThrows(IllegalArgumentException::class.java) { data.get(-1) }
    }

    @Test
    fun whenSetMultipleTimes_ProvidesMostRecentValue() {
        val data = createHistoricalData(0)
        val mostRecentValue = 2

        data.set(1)
        data.set(mostRecentValue)

        Assertions.assertEquals(mostRecentValue, data.get())
    }

    @Test
    fun whenSetMultipleTimes_CanStillAccessHistoricalValues() {
        val data = createHistoricalData(0)

        data.set(2)

        val firstPastValue = 1
        Assertions.assertEquals(0, data.get(firstPastValue))
    }

    @Test
    fun whenGivenPastLongerThanHistory_ReturnsOldestValue() {
        val oldestValue = 0
        val data = createHistoricalData(oldestValue)

        data.set(1)

        Assertions.assertEquals(oldestValue, data.get(5))
    }

    @Test
    fun tracksMaxHistoryAmountOfValues() {
        val data = createHistoricalData(0, maxHistory = 4)

        data.set(1)
        data.set(2)
        data.set(3)

        Assertions.assertEquals(3, data.get())
        Assertions.assertEquals(2, data.get(1))
        Assertions.assertEquals(1, data.get(2))
        Assertions.assertEquals(0, data.get(3))
    }

    @Test
    fun whenMaxHistoryLessThan2_ThrowsError() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { createHistoricalData(0, 1) }
    }

    private fun createHistoricalData(defaultValue: Int, maxHistory: Int = 3) = HistoricalData(defaultValue, maxHistory)
}