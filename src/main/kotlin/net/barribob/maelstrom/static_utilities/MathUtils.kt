package net.barribob.maelstrom.static_utilities

import net.barribob.maelstrom.general.math.ReferencedAxisRotator
import net.minecraft.util.math.Box
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.*

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

    fun circleCallback(radius: Double, points: Int, axis: Vec3d, callback: (Vec3d) -> Unit) {
        val degrees = Math.PI * 2 / points
        val axisYaw = directionToYaw(axis)
        val rotator = ReferencedAxisRotator(VecUtils.yAxis, axis)
        for (i in 0 until points) {
            val radians = i * degrees
            val offset = Vec3d(sin(radians), 0.0, cos(radians))
                .multiply(radius)
                .rotateVector(VecUtils.yAxis, -axisYaw)
            val rotated = rotator.rotate(offset)
            callback(rotated)
        }
    }

    fun circlePoints(radius: Double, points: Int, axis: Vec3d): Collection<Vec3d> {
        val vectors = mutableListOf<Vec3d>()
        circleCallback(radius, points, axis, vectors::add)
        return vectors
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

    fun directionToYaw(direction: Vec3d): Double {
        val x: Double = direction.x
        val z: Double = direction.z

        return Math.toDegrees(MathHelper.atan2(z, x))
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

    fun facingSameDirection(direction1: Vec3d, direction2: Vec3d): Boolean {
        return direction1.dotProduct(direction2) > 0
    }

    // https://www.wikihow.com/Add-Consecutive-Integers-from-1-to-100
    fun consecutiveSum(firstNumber: Int, lastNumber: Int): Int {
        return ((lastNumber - firstNumber + 1) * ((firstNumber + lastNumber) * 0.5f)).toInt()
    }

    fun roundedStep(n: Float, steps: List<Float>, floor: Boolean = false): Float {
        return if(floor) {
            steps.sortedDescending().firstOrNull { it <= n } ?: steps.first()
        } else {
            steps.sorted().firstOrNull { it > n } ?: steps.last()
        }
    }
}