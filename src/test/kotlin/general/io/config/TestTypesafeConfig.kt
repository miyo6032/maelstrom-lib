package general.io.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import testing_utilities.TestConfigFactory

class TestTypesafeConfig {
    @Test
    fun getConfigList_ConvertsObjects() {
        val config = TestConfigFactory.createConfig("list=[{test=1}, {test=2}]")
        val configList = config.getConfigList("list")

        Assertions.assertEquals(2, configList.size)
        Assertions.assertTrue(configList[0].hasPath("test"))
        Assertions.assertTrue(configList[1].hasPath("test"))
    }
}