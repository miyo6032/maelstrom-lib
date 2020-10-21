package static_utilities

import io.netty.buffer.Unpooled
import net.barribob.maelstrom.static_utilities.readFloatList
import net.barribob.maelstrom.static_utilities.writeFloatList
import net.minecraft.network.PacketByteBuf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestPacketUtils {
    @Test
    fun readFloatList_Throws_WhenCountNegative() {
        val packet = PacketByteBuf(Unpooled.buffer())
        Assertions.assertThrows(IllegalArgumentException::class.java) { packet.readFloatList(-1) }
    }

    @Test
    fun readFloatList_ReturnsList() {
        val packet = PacketByteBuf(Unpooled.buffer())
        packet.writeFloat(3f)
        packet.writeFloat(3f)
        val read = packet.readFloatList(2)
        Assertions.assertEquals(listOf(3f, 3f), read)
    }

    @Test
    fun writeFloatList_WritesToPacket() {
        val packet = PacketByteBuf(Unpooled.buffer())
        val expected = listOf(3f, 3f)
        packet.writeFloatList(expected)
        val actual = packet.readFloatList(2)
        Assertions.assertEquals(expected, actual)
    }
}