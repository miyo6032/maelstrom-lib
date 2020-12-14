package net.barribob.maelstrom.static_utilities

import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.util.shape.VoxelShape
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

fun Box.corners() = listOf(
    Vec3d(this.minX, this.maxY, this.minZ),
    Vec3d(this.maxX, this.maxY, this.minZ),
    Vec3d(this.minX, this.maxY, this.maxZ),
    Vec3d(this.maxX, this.maxY, this.maxZ),
    Vec3d(this.minX, this.minY, this.minZ),
    Vec3d(this.maxX, this.minY, this.minZ),
    Vec3d(this.minX, this.minY, this.maxZ),
    Vec3d(this.maxX, this.minY, this.maxZ)
)

// https://stackoverflow.com/questions/23086291/format-in-kotlin-string-templates
fun Double.format(digits: Int) = "%.${digits}f".format(this)

object MathUtils {
    /**
     * Treats input as a vector and finds the length of that vector
     */
    // TODO: Unit test
    fun magnitude(vararg values: Double): Double {
        var sum = 0.0
        for (value in values) {
            sum += value.pow(2.0)
        }
        return sqrt(sum)
    }

    @Deprecated("Specific to jumping ai")
    fun findClosestCorner(point: Vec3d, shape: VoxelShape, maxSamples: Int): Vec3d? {
        val corners = shape.boundingBoxes.flatMap { getTopCornersAndEdges(it) }.shuffled().take(maxSamples)
        return corners.minBy { it.squaredDistanceTo(point) }
    }

    private fun getTopCornersAndEdges(box: Box): List<Vec3d> {
        val halfX = box.xLength * 0.5
        val halfZ = box.zLength * 0.5

        return listOf(
            Vec3d(box.minX, box.maxY, box.minZ),
            Vec3d(box.maxX, box.maxY, box.minZ),
            Vec3d(box.minX, box.maxY, box.maxZ),
            Vec3d(box.maxX, box.maxY, box.maxZ),
            Vec3d(box.minX + halfX, box.maxY, box.minZ),
            Vec3d(box.minX, box.maxY, box.minZ + halfZ),
            Vec3d(box.maxX, box.maxY, box.minZ + halfZ),
            Vec3d(box.minX + halfX, box.maxY, box.maxZ)
        )
    }

    fun withinDistance(pos1: Vec3d, pos2: Vec3d, distance: Double): Boolean {
        if (distance < 0) throw IllegalArgumentException("Distance cannot be negative")
        return pos1.squaredDistanceTo(pos2) < distance.pow(2.0)
    }

    fun movingTowards(center: Vec3d, pos: Vec3d, direction: Vec3d): Boolean {
        val directionTo = unNormedDirection(pos, center)
        return direction.dotProduct(directionTo) > 0
    }

    fun unNormedDirection(source: Vec3d, target: Vec3d): Vec3d = target.subtract(source)

    /**
     * Calls a function that linearly interpolates between two points. Includes both ends of the line
     *
     * Callback returns the position and the point number from 1 to points
     */
    fun lineCallback(start: Vec3d, end: Vec3d, points: Int, callback: (Vec3d, Int) -> Unit) {
        val dir: Vec3d = end.subtract(start).multiply(1 / (points - 1).toDouble())
        var pos = start
        for (i in 0 until points) {
            callback(pos, i)
            pos = pos.add(dir)
        }
    }

    fun willBoxFit(box: Box, movement: Vec3d, collision: (Box) -> Boolean): Boolean {
        var collided = false
        val points = ceil(movement.length() / box.averageSideLength).toInt()
        lineCallback(Vec3d.ZERO, movement, points) { vec3d, _ ->
            if (collision(box.offset(vec3d))) {
                collided = true
            }
        }

        return !collided
    }

    fun directionToPitch(direction: Vec3d): Float {
        val x: Double = direction.x
        val z: Double = direction.z
        val y: Double = direction.y

        val h = MathHelper.sqrt(x * x + z * z).toDouble()
        return (Math.toDegrees(-(MathHelper.atan2(y, h)))).toFloat()
    }

    fun lerpVec(partialTicks: Float, vec1: Vec3d, vec2: Vec3d): Vec3d {
        val pt = partialTicks.toDouble()
        val x = MathHelper.lerp(pt, vec1.x, vec2.x)
        val y = MathHelper.lerp(pt, vec1.y, vec2.y)
        val z = MathHelper.lerp(pt, vec1.z, vec2.z)
        return Vec3d(x, y, z)
    }

    fun axisOffset(direction: Vec3d, offset: Vec3d): Vec3d {
        val forward: Vec3d = direction.normalize()
        val side: Vec3d = forward.crossProduct(VecUtils.yAxis).normalize()
        val up: Vec3d = side.crossProduct(forward).normalize()
        return forward.multiply(offset.x).add(side.multiply(offset.z)).add(up.multiply(offset.y))
    }
}