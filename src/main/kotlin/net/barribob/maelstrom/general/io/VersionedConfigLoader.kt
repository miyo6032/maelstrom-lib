package net.barribob.maelstrom.general.io

import net.barribob.maelstrom.util.Version

class VersionedConfigLoader(
    private val logger: ILogger,
    private val versionFactory: IVersionFactory,
    private val fileManager: IFileManager
) {

    fun handleConfigLoad(modId: String, configName: String): IVersion {
        val configDirectoryPath = "./config/$modId/"
        val defaultConfigPath = "default_configs/${configName}_default.conf"
        val defaultConfig = versionFactory.loadFromSrc(defaultConfigPath)

        if (!fileManager.exists(configDirectoryPath)) {
            if (!fileManager.mkdirs(configDirectoryPath)) {
                logger.error("Failed to create directory for config file: $configName")
                return defaultConfig
            }
        }

        val runConfig = "$configDirectoryPath$configName.conf"
        val config = versionFactory.loadFromRun(runConfig)

        if (fileManager.exists(runConfig)) {

            val defaultVersion = Version(defaultConfig.getVersion())

            val configVersion: Version = try {
                Version(config.getVersion())
            } catch (e: IllegalArgumentException) {
                logger.warn("Config file, $configName has a broken version. Using default.")
                return defaultConfig
            }

            if (defaultVersion == configVersion) {
                return config
            } else {
                val backupPath = "$configDirectoryPath${configName}_${configVersion.get()}.conf"
                fileManager.copy(runConfig, backupPath)
                logger.warn("Config file, $configName is outdated. Created backup of config ($backupPath), and using new default.")
            }
        }

        fileManager.copyFromSrc(defaultConfigPath, runConfig)
        return defaultConfig
    }
}