package static_utilities

import net.barribob.maelstrom.static_utilities.ClientServerUtils
import net.minecraft.util.math.Vec3d
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestClientUtils {
    @Test
    fun drawDebugPoints_SerializesCorrectly() {
        val time = 1
        val color = listOf(1f, 1f, 1f, 1f)
        val points = listOf(Vec3d.ZERO)
        val packetData = ClientServerUtils.packDrawDebugPoints(time, color, points)
        val out = ClientServerUtils.unpackDrawDebugPoints(packetData)
        Assertions.assertEquals(time, out.first)
        Assertions.assertEquals(color, out.second)
        Assertions.assertEquals(listOf(0f, 0f, 0f), out.third)
    }

    @Test
    fun drawDebugPoints_ThrowsError_WhenColorIncorrectLength() {
        val time = 1
        val color = listOf(1f, 1f, 1f)
        val points = listOf(Vec3d.ZERO)
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            ClientServerUtils.packDrawDebugPoints(
                time,
                color,
                points
            )
        }
    }
}