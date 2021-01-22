package general.data

import net.barribob.maelstrom.general.data.BooleanFlag
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestBooleanFlag {

    @Test
    fun getAndReset_returnsFalse_ByDefault() {
        val flag = createBooleanFlag()

        Assertions.assertFalse(flag.getAndReset())
    }

    @Test
    fun getAndReset_returnsTrue_WhenFlagged() {
        val flag = createBooleanFlag()

        flag.flag()

        Assertions.assertTrue(flag.getAndReset())
    }

    @Test
    fun getAndReset_WhenCalled_ResetsToFalse() {
        val flag = createBooleanFlag()

        flag.flag()
        flag.getAndReset()

        Assertions.assertFalse(flag.getAndReset())
    }

    private fun createBooleanFlag() = BooleanFlag()
}