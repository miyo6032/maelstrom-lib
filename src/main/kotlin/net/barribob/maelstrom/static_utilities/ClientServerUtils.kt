package net.barribob.maelstrom.static_utilities

import io.netty.buffer.Unpooled
import net.barribob.maelstrom.MaelstromMod
import net.barribob.maelstrom.general.event.TimedEvent
import net.barribob.maelstrom.render.RenderData
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.PacketContext
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

object ClientServerUtils {
    fun drawDebugPoints(points: List<Vec3d>, color: List<Float>, time: Int, watchPoint: Vec3d, world: World) {
        val packetData = packDrawDebugPoints(time, color, points)

        PlayerStream.around(world, watchPoint, 100.0).forEach {
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(
                it,
                MaelstromMod.DRAW_POINTS_PACKET_ID,
                packetData
            )
        }
    }

    fun packDrawDebugPoints(time: Int, color: List<Float>, points: List<Vec3d>): PacketByteBuf {
        if (color.size != 4) throw IllegalArgumentException("Color needs to be defined with 4 floats")
        val packetData = PacketByteBuf(Unpooled.buffer())
        packetData.writeInt(time)
        packetData.writeInt(points.size)
        packetData.writeFloatList(color)
        packetData.writeFloatList(points.flatMap { listOf(it.x, it.y, it.z) }.map(Double::toFloat))
        return packetData
    }

    fun unpackDrawDebugPoints(packetData: PacketByteBuf): Triple<Int, List<Float>, List<Float>> {
        val delay = packetData.readInt()
        val numPoints = packetData.readInt()
        val colorArray = packetData.readFloatList(4)
        val pointArray = packetData.readFloatList(numPoints * 3)
        return Triple(delay, colorArray, pointArray)
    }

    @Environment(EnvType.CLIENT)
    fun drawDebugPointsClient(packetContext: PacketContext, packetData: PacketByteBuf) {
        val (delay, colorArray, pointArray) = unpackDrawDebugPoints(packetData)

        packetContext.taskQueue.execute {
            val renderer = { renderData: RenderData -> RenderUtils.renderPoints(renderData, colorArray, pointArray) }
            val removeEvent: () -> Unit = { MaelstromMod.renderMap.renderMap.remove(renderer) }

            MaelstromMod.renderMap.renderMap.add(renderer)
            MaelstromMod.clientEventScheduler.addEvent(TimedEvent(removeEvent, delay))
        }
    }
}