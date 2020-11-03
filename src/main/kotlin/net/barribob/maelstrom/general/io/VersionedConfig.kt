package net.barribob.maelstrom.general.io

import com.typesafe.config.Config


class VersionedConfig(val config: Config) : IVersion {
    override fun getVersion(): String = if (config.hasPath("config_version")) config.getString("config_version") else ""
}