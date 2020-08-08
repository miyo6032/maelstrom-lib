package net.barribob.maelstrom.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.barribob.maelstrom.MaelstromMod;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class HoconConfigManager {
    public Config handleConfigLoad(String modId, String configName) {
        String defaultConfigPath = "default_configs/" + configName + "_default.conf";
        Config defaultConfig = ConfigFactory.load(defaultConfigPath);

        String configDirectoryPath = "./config/" + modId + "/";
        File configDirectory = new File(configDirectoryPath);

        if (!configDirectory.exists()) {
            if (!configDirectory.mkdirs()) {
                MaelstromMod.INSTANCE.getLOGGER().error("Failed to create directory for config file: " + configName);
                return defaultConfig;
            }
        }

        File configFile = new File(configDirectoryPath + configName + ".conf");

        if (configFile.exists()) {
            try {
                Config config = ConfigFactory.parseFile(configFile);
                config.checkValid(defaultConfig, "data");

                Version defaultVersion = new Version(defaultConfig.getString("config_version"));
                Version configVersion = new Version(config.getString("config_version"));

                if (defaultVersion.compareTo(configVersion) <= 0) {
                    return config;
                } else {
                    File configBackup = new File(configDirectory, "config_" + configVersion.get() + ".conf");
                    Files.copy(configFile.toPath(), configBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    MaelstromMod.INSTANCE.getLOGGER().warn("Config file, " + configFile + " is outdated. Created backup of config (" + configBackup.toString() + "), and using new default.");
                }
            } catch (IllegalArgumentException | IOException e) {
                MaelstromMod.INSTANCE.getLOGGER().error("Failed to read config file for " + configFile + "!");
                MaelstromMod.INSTANCE.getLOGGER().error(e.toString());
                return defaultConfig;
            }
        }

        CopyDefaultConfig(defaultConfigPath, configFile);

        return defaultConfig;
    }

    public Config loadDefaultConfig(String configName) {
        String defaultConfigPath = "default_configs/" + configName + "_default.conf";
        return ConfigFactory.load(defaultConfigPath);
    }

    private void CopyDefaultConfig(String defaultConfigPath, File configFile) {
        InputStream defaultResourcePath = this.getClass().getClassLoader().getResourceAsStream(defaultConfigPath);
        if (defaultResourcePath != null) {
            try {
                Files.copy(defaultResourcePath, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                MaelstromMod.INSTANCE.getLOGGER().error("Failed to write new config file: " + defaultConfigPath);
                MaelstromMod.INSTANCE.getLOGGER().error(e.toString());
            }
        } else {
            MaelstromMod.INSTANCE.getLOGGER().error("Failed to get default config to copy: " + defaultConfigPath);
        }
    }
}