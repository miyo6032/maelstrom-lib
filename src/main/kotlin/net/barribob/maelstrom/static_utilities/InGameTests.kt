package net.barribob.maelstrom.static_utilities

import net.minecraft.entity.Entity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.explosion.Explosion

object InGameTests {
    fun lineCallback(source: ServerCommandSource) {
        val entity = source.entityOrThrow
        val direction = entity.rotationVector.multiply(3.0)
        val linePoints = mutableListOf<Vec3d>()
        val pos = entity.getCameraPosVec(1f)
        MathUtils.lineCallback(pos, pos.add(direction), 10) { v, _ -> linePoints.add(v) }
        ClientServerUtils.drawDebugPoints(linePoints, 1, pos, entity.world)
    }

    fun boxCorners(source: ServerCommandSource) {
        val entity = source.entityOrThrow
        val box = entity.boundingBox
        ClientServerUtils.drawDebugPoints(box.corners(), 40, entity.pos, entity.world)
    }

    fun willBoxFit(source: ServerCommandSource) {
        val entity = source.entityOrThrow
        val look = entity.rotationVector
        val boxSize = 0.5
        val lookOffset = look.multiply(3.0)
        val minVec = entity.getCameraPosVec(1.0f).add(lookOffset)
        val box = Box(minVec, minVec.add(VecUtils.unit.multiply(boxSize))).offset(VecUtils.unit.multiply(-boxSize * 0.5))
        val scanDistance = look.multiply(3.0)

        MathUtils.willBoxFit(box, scanDistance) { b ->
            val fits = entity.world.isSpaceEmpty(entity, b)
            val color = if (fits) listOf(1f, 1f, 1f, 1f) else listOf(1f, 0f, 0f, 1f)
            ClientServerUtils.drawDebugPoints(b.corners(), 1, entity.pos, entity.world, color)
            return@willBoxFit !fits
        }
    }

    fun raycast(source: ServerCommandSource) {
        val entity = source.entityOrThrow
        val (pos, target) = getLineOffsets(entity)

        val blockCollision = entity.world.raycast(RaycastContext(pos, target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, entity))
        val collided = blockCollision.type != HitResult.Type.MISS

        val color = if (!collided) listOf(1f, 1f, 1f, 1f) else listOf(1f, 0f, 0f, 1f)
        drawLine(pos, target, entity, color)
    }

    fun explode(source: ServerCommandSource) {
        val e = source.entityOrThrow
        source.world.createExplosion(e, e.x, e.y, e.z, 9f, Explosion.DestructionType.DESTROY)
    }

    private fun drawLine(
        pos: Vec3d,
        target: Vec3d,
        entity: Entity,
        color: List<Float>
    ) {
        val linePoints = mutableListOf<Vec3d>()
        MathUtils.lineCallback(pos, target, 100) { v, _ -> linePoints.add(v) }
        ClientServerUtils.drawDebugPoints(linePoints, 1, pos, entity.world, color)
    }


    private fun getLineOffsets(entity: Entity, offset: Double = 3.0): Pair<Vec3d, Vec3d> {
        val look = entity.rotationVector
        val lookOffset = look.multiply(offset)
        val pos = entity.pos.add(lookOffset)
        val target = pos.add(lookOffset)
        return Pair(pos, target)
    }
}