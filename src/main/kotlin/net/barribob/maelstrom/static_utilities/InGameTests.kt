package net.barribob.maelstrom.static_utilities

import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.math.Vec3d

object InGameTests {
    fun lineCallback(source: ServerCommandSource) {
        val entity = source.entityOrThrow
        val direction = entity.rotationVector.multiply(3.0)
        val linePoints = mutableListOf<Vec3d>()
        val pos = entity.getCameraPosVec(1f)
        VecUtils.lineCallback(pos, pos.add(direction), 10) { v, _ -> linePoints.add(v) }
        ClientServerUtils.drawDebugPoints(linePoints, 1, pos, entity.world)
    }
}