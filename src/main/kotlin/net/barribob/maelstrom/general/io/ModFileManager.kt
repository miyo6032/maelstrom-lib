package net.barribob.maelstrom.general.io

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class ModFileManager(private val logger: ILogger) : IFileManager {
    override fun mkdirs(filePath: String): Boolean = File(filePath).mkdirs()

    override fun exists(filePath: String): Boolean = File(filePath).exists()

    override fun copy(fromFilePath: String, toFilePath: String) {
        try {
            Files.copy(Paths.get(fromFilePath), Paths.get(toFilePath), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            logger.error("Failed to copy file $fromFilePath to file $toFilePath")
            logger.error(e.toString())
        }
    }

    override fun copyFromSrc(fromFilePath: String, toFilePath: String) {
        val defaultResourcePath = javaClass.classLoader.getResourceAsStream(fromFilePath)
        if (defaultResourcePath != null) {
            try {
                Files.copy(defaultResourcePath, Paths.get(toFilePath), StandardCopyOption.REPLACE_EXISTING)
            } catch (e: IOException) {
                logger.error("Failed to copy src file $fromFilePath to run file $toFilePath")
                logger.error(e.toString())
            }
        } else {
            logger.error("Failed to get file to copy: $fromFilePath")
        }
    }
}