package net.barribob.maelstrom.general.io

import com.typesafe.config.ConfigFactory
import java.io.File

class ConfigVersionFactory : IVersionFactory <VersionedConfig> {
    override fun loadFromSrc(baseResourceName: String): VersionedConfig {
        return VersionedConfig(ConfigFactory.load(baseResourceName))
    }

    override fun loadFromRun(pathName: String): VersionedConfig {
        val configFile = File(pathName)
        if (configFile.exists()) {
            return VersionedConfig(ConfigFactory.parseFile(configFile))
        }

        return VersionedConfig(ConfigFactory.empty())
    }
}