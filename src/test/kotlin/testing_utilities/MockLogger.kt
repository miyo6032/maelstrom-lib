package testing_utilities

import net.barribob.maelstrom.general.io.ILogger

class MockLogger : ILogger {
    val errorMessages = mutableListOf<String>()
    val warningMessages = mutableListOf<String>()
    val infoMessages = mutableListOf<String>()

    override fun error(message: Any) {
        errorMessages.add(message.toString())
    }

    override fun warn(message: Any) {
        warningMessages.add(message.toString())
    }

    override fun info(message: Any) {
        infoMessages.add(message.toString())
    }
}