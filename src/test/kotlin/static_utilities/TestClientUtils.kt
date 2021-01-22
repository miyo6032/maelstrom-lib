package static_utilities

import net.barribob.maelstrom.static_utilities.DebugPointsNetworkHandler
import net.minecraft.util.math.Vec3d
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestClientUtils {
    @Test
    fun drawDebugPoints_SerializesCorrectly() {
        val debugPointsNetworkHandler = DebugPointsNetworkHandler()
        val time = 1
        val color = listOf(1f, 1f, 1f, 1f)
        val points = listOf(Vec3d.ZERO)
        val packetData = debugPointsNetworkHandler.packDrawDebugPoints(time, color, points)
        val out = debugPointsNetworkHandler.unpackDrawDebugPoints(packetData)
        Assertions.assertEquals(time, out.first)
        Assertions.assertEquals(color, out.second)
        Assertions.assertEquals(listOf(0f, 0f, 0f), out.third)
    }

    @Test
    fun drawDebugPoints_ThrowsError_WhenColorIncorrectLength() {
        val debugPointsNetworkHandler = DebugPointsNetworkHandler()
        val time = 1
        val color = listOf(1f, 1f, 1f)
        val points = listOf(Vec3d.ZERO)
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            debugPointsNetworkHandler.packDrawDebugPoints(
                time,
                color,
                points
            )
        }
    }
}