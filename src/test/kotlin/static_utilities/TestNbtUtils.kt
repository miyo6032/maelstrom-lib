package static_utilities

import net.barribob.maelstrom.static_utilities.NbtUtils
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import testing_utilities.MockLogger
import testing_utilities.TestConfigFactory

class TestNbtUtils {
    @Test
    fun readDefaultNbt_WhenEmpty_ReturnsEmpty() {
        val config = TestConfigFactory.createConfig("")
        val result = NbtUtils.readDefaultNbt(MockLogger(), config)

        assertTrue(result.isEmpty)
    }

    @Test
    fun readDefaultNbt_ReadsNbtProperties() {
        val config = TestConfigFactory.createConfig("{ value: 1.0 }")
        val result = NbtUtils.readDefaultNbt(MockLogger(), config)

        assertNotNull(result)
        assertTrue(result.contains("value"))
    }
}