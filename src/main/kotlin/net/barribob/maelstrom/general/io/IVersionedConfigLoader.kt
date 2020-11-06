package net.barribob.maelstrom.general.io

interface IVersionedConfigLoader<T : IVersion> {
    fun handleConfigLoad(modId: String, configName: String): T
}