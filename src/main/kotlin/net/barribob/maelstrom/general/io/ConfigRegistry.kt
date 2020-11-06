package net.barribob.maelstrom.general.io

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.minecraft.util.Identifier

class ConfigRegistry (private val versionedConfigLoader: IVersionedConfigLoader<VersionedConfig>) {
    private val configsToLoad = mutableSetOf<Identifier>()
    private var loadedConfigs = mutableMapOf<Identifier, Config>()

    fun reloadConfigs() {
        loadedConfigs = mutableMapOf()
        configsToLoad.forEach { loadedConfigs[it] = versionedConfigLoader.handleConfigLoad(it.namespace, it.path).config }
    }

    fun getConfig(identifier: Identifier) : Config {
        if (!configsToLoad.contains(identifier)) {
            configsToLoad.add(identifier)
        }

        if (!loadedConfigs.containsKey(identifier)) {
            loadedConfigs[identifier] = versionedConfigLoader.handleConfigLoad(identifier.namespace, identifier.path).config
        }

        return loadedConfigs.getOrDefault(identifier, ConfigFactory.empty())
    }
}