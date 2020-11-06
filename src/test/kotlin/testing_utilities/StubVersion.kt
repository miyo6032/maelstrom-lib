package testing_utilities

import net.barribob.maelstrom.general.io.IVersion

class StubVersion(private val ver: String) : IVersion {
    override fun getVersion(): String = ver
}