package net.barribob.maelstrom.general.io

import com.typesafe.config.ConfigFactory
import net.barribob.maelstrom.general.io.config.TypesafeConfig
import java.io.File

class ConfigVersionFactory : IVersionFactory <VersionedConfig> {
    override fun loadFromSrc(baseResourceName: String): VersionedConfig {
        return VersionedConfig(TypesafeConfig(ConfigFactory.load(baseResourceName)))
    }

    override fun loadFromRun(pathName: String): VersionedConfig {
        val configFile = File(pathName)
        if (configFile.exists()) {
            return VersionedConfig(TypesafeConfig(ConfigFactory.parseFile(configFile)))
        }

        return VersionedConfig(TypesafeConfig(ConfigFactory.empty()))
    }
}