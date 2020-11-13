package testing_utilities

import com.typesafe.config.ConfigFactory
import net.barribob.maelstrom.general.io.config.TypesafeConfig

object TestConfigFactory {
    fun createConfig(configText: String) = TypesafeConfig(ConfigFactory.parseString(configText))
}