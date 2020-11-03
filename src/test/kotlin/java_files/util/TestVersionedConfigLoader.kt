package java_files.util

import net.barribob.maelstrom.general.io.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import testing_utilities.MockLogger

class TestVersionedConfigLoader {
    private val modId = "modid"
    private val config = "testConfig"

    @Test
    fun handleConfigLoad_WhenVersionsSame_LoadsRunVersion() {
        val data = runConfigLoad("1.0", "1.0", true)

        Assertions.assertEquals(data.run, data.result)
    }

    @Test
    fun handleConfigLoad_WhenRunMissing_LoadsDefaultVersion() {
        val data = runConfigLoad("1.0", "1.0", false)

        Assertions.assertEquals(data.default, data.result)
    }

    @Test
    fun handleConfigLoad_WhenVersionsDifferent_LoadsDefaultVersion() {
        val data = runConfigLoad("1", "0", true)

        Assertions.assertEquals("0", data.result.getVersion())
    }

    @Test
    fun handleConfigLoad_WhenRunVersionBroken_LoadsDefaultVersion() {
        val data = runConfigLoad("yeet!", "0", true)

        Assertions.assertEquals("0", data.result.getVersion())
    }

    @Test
    fun handleConfigLoad_WhenDefaultVersionBroken_ThrowsException() {
        Assertions.assertThrows(Exception::class.java) { runConfigLoad("1", "yooo!", true) }
    }

    @Test
    fun handleConfigLoad_WhenVersionsDifferent_CopiesBackup() {
        val data = runConfigLoad("1", "0", true)

        Assertions.assertEquals(data.fileManager.copies[0], Pair(data.runFilePath, data.backupFilePath))
    }

    @Test
    fun handleConfigLoad_WhenVersionsDifferent_CopiesDefault() {
        val data = runConfigLoad("1", "0", true)

        Assertions.assertEquals(data.fileManager.sourceCopies[0], Pair(data.defaultFilePath, data.runFilePath))
    }

    @Test
    fun handleConfigLoad_WhenRunMissing_CopiesDefault() {
        val data = runConfigLoad("1", "0", false)

        Assertions.assertEquals(data.fileManager.sourceCopies[0], Pair(data.defaultFilePath, data.runFilePath))
    }

    @Test
    fun handleConfigLoad_WhenVersionsDifferent_EmitsWarning() {
        val data = runConfigLoad("1", "0", true)

        assertThat(data.logger.warningMessages, hasItem("Config file, $config is outdated. Created backup of config (./config/$modId/${config}_1.conf), and using new default."))
    }

    @Test
    fun handleConfigLoad_WhenRunVersionBroken_EmitsWarning() {
        val data = runConfigLoad("yeet!", "0", true)

        assertThat(data.logger.warningMessages, hasItem("Config file, $config has a broken version. Using default."))
    }

    private fun runConfigLoad(runVersion: String, defaultVersion: String, exists: Boolean): TestData {
        val defaultFilePath = "default_configs/${config}_default.conf"
        val runFilePath = "./config/$modId/${config}.conf"
        val backupFilePath = "./config/$modId/${config}_${runVersion}.conf"

        val default = StubVersion(defaultVersion)
        val run = StubVersion(runVersion)
        val fileManager = MockFileManager(exists)
        val configFactory = StubVersionFactory(default, run)
        val logger = MockLogger()
        val manager = VersionedConfigLoader(logger, configFactory, fileManager)

        val result = manager.handleConfigLoad(modId, config)

        return TestData(defaultFilePath, runFilePath, backupFilePath, fileManager, result, default, run, logger)
    }

    data class TestData(
        val defaultFilePath: String,
        val runFilePath: String,
        val backupFilePath: String,
        val fileManager: MockFileManager,
        val result: IVersion,
        val default: IVersion,
        val run: IVersion,
        val logger: MockLogger
    )

    class StubVersion(private val ver: String) : IVersion {
        override fun getVersion(): String = ver
    }

    class StubVersionFactory(private val default: IVersion, private val runConfig: IVersion) : IVersionFactory {
        override fun loadFromSrc(baseResourceName: String): IVersion = default
        override fun loadFromRun(pathName: String): IVersion = runConfig
    }

    class MockFileManager(private val exists: Boolean) : IFileManager {
        val copies = mutableListOf<Pair<String, String>>()
        val sourceCopies = mutableListOf<Pair<String, String>>()

        override fun mkdirs(filePath: String): Boolean = true

        override fun exists(filePath: String): Boolean = exists

        override fun copy(fromFilePath: String, toFilePath: String) {
            copies.add(Pair(fromFilePath, toFilePath))
        }

        override fun copyFromSrc(fromFilePath: String, toFilePath: String) {
            sourceCopies.add(Pair(fromFilePath, toFilePath))
        }
    }
}