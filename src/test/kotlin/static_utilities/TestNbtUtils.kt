package static_utilities

import com.typesafe.config.ConfigFactory
import net.barribob.maelstrom.static_utilities.NbtUtils
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import testing_utilities.MockLogger

class TestNbtUtils {
    @Test
    fun readDefaultNbt_WhenEmpty_ReturnsEmpty() {
        val config = ConfigFactory.parseString("")
        val result = NbtUtils.readDefaultNbt(MockLogger(), config)

        assertTrue(result.isEmpty)
    }

    @Test
    fun readDefaultNbt_ReadsNbtProperties() {
        val config = ConfigFactory.parseString("{ value: 1.0 }")
        val result = NbtUtils.readDefaultNbt(MockLogger(), config)

        assertNotNull(result)
        assertTrue(result.contains("value"))
    }
}