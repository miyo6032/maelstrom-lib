package static_utilities

import net.barribob.maelstrom.static_utilities.RandomUtils
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestRandomUtils {
    @Test
    fun range_WhenMinLargerThanMax_ThrowsError() {
        val exception = Assertions.assertThrows(IllegalArgumentException::class.java){ RandomUtils.range(1, 0) }

        assertThat(exception.message, containsString("Minimum is greater than maximum"))
    }
}