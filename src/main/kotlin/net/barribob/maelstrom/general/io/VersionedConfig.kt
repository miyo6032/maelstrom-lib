package net.barribob.maelstrom.general.io

import net.barribob.maelstrom.general.io.config.IConfig

class VersionedConfig(val config: IConfig) : IVersion {
    override fun getVersion(): String = if (config.hasPath("config_version")) config.getString("config_version") else ""
}