package general.io

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.barribob.maelstrom.general.io.ConfigRegistry
import net.barribob.maelstrom.general.io.IVersionedConfigLoader
import net.barribob.maelstrom.general.io.VersionedConfig
import net.minecraft.util.Identifier
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestConfigRegistry {
    private val identifier = Identifier("modid", "config")

    @Test
    fun getConfig_ProvidesConfig() {
        val config = createConfig("test=1")
        val version = createVersionedConfig(config)
        val versionLoader = StubVersionLoader(version)
        val configRegistry = createConfigRegistry(versionLoader)

        val result = configRegistry.getConfig(identifier)
        assertEquals(config, result)
    }

    @Test
    fun getConfig_WhenReloaded_ProvidesUpdatedConfig() {
        val config = createConfig("test=1")
        val updatedConfig = createConfig("test=updated")
        val version = createVersionedConfig(config)
        val updateVersion = createVersionedConfig(updatedConfig)
        val versionLoader = ReloadVersionLoader(version, updateVersion)
        val configRegistry = createConfigRegistry(versionLoader)

        configRegistry.getConfig(identifier)
        configRegistry.reloadConfigs()
        val result = configRegistry.getConfig(identifier)
        assertEquals(updatedConfig, result)
    }

    private fun createConfig(configText: String) = ConfigFactory.parseString(configText)
    private fun createVersionedConfig(config: Config) = VersionedConfig(config)

    private fun createConfigRegistry(versionLoader: IVersionedConfigLoader<VersionedConfig>): ConfigRegistry {
        return ConfigRegistry(versionLoader)
    }

    class StubVersionLoader(private val version: VersionedConfig) : IVersionedConfigLoader<VersionedConfig> {
        override fun handleConfigLoad(modId: String, configName: String): VersionedConfig = version
    }

    class ReloadVersionLoader(private val version: VersionedConfig, private val reloadVersion: VersionedConfig) :
        IVersionedConfigLoader<VersionedConfig> {
        private var timeReloaded = 0
        override fun handleConfigLoad(modId: String, configName: String): VersionedConfig {
            timeReloaded++
            return if (timeReloaded == 1) {
                version
            } else {
                reloadVersion
            }
        }
    }
}