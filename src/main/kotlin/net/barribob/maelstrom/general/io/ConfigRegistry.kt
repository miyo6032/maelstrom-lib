package net.barribob.maelstrom.general.io

import net.barribob.maelstrom.general.io.config.IConfig
import net.minecraft.util.Identifier

class ConfigRegistry (private val versionedConfigLoader: IVersionedConfigLoader<VersionedConfig>) {
    private val configsToLoad = mutableSetOf<Identifier>()
    private var loadedConfigs = mutableMapOf<Identifier, IConfig>()

    fun reloadConfigs() {
        loadedConfigs = mutableMapOf()
        configsToLoad.forEach { loadedConfigs[it] = versionedConfigLoader.handleConfigLoad(it.namespace, it.path).config }
    }

    fun getConfig(identifier: Identifier) : IConfig {
        if (!configsToLoad.contains(identifier)) {
            configsToLoad.add(identifier)
        }

        if (!loadedConfigs.containsKey(identifier)) {
            loadedConfigs[identifier] = versionedConfigLoader.handleConfigLoad(identifier.namespace, identifier.path).config
        }

        return loadedConfigs[identifier]
            ?: throw Exception("Error trying to get configuration from registry: $identifier")
    }
}