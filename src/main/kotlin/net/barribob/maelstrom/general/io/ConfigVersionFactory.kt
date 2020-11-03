package net.barribob.maelstrom.general.io

import com.typesafe.config.ConfigFactory
import java.io.File

class ConfigVersionFactory : IVersionFactory {
    override fun loadFromSrc(baseResourceName: String): IVersion {
        return VersionedConfig(ConfigFactory.load(baseResourceName))
    }

    override fun loadFromRun(pathName: String): IVersion {
        val configFile = File(pathName)
        if (configFile.exists()) {
            return VersionedConfig(ConfigFactory.parseFile(configFile))
        }

        return VersionedConfig(ConfigFactory.empty())
    }
}