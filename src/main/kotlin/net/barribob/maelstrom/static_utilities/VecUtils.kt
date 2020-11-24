package net.barribob.maelstrom.static_utilities

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import kotlin.math.cos
import kotlin.math.sin

fun Vec3d.yOffset(i: Double): Vec3d = this.add(0.0, i, 0.0)

fun Vec3d.planeProject(planeVector: Vec3d): Vec3d = this.subtract(planeVector.multiply(this.dotProduct(planeVector)))

fun newVec3d(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Vec3d = Vec3d(x, y, z)

fun BlockPos.asVec3d(): Vec3d = Vec3d(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())

fun Vec3d.negateServer(): Vec3d = this.multiply(-1.0) // Why do you force me to do this mojang

/**
 * Rotate a vector around an axis by given degrees https://stackoverflow.com/questions/31225062/rotating-a-vector-by-angle-and-axis-in-java
 */
fun Vec3d.rotateVector(axis: Vec3d, degrees: Double): Vec3d {
    val theta = Math.toRadians(degrees)
    val normedAxis = axis.normalize()
    val x: Double = this.x
    val y: Double = this.y
    val z: Double = this.z
    val u: Double = normedAxis.x
    val v: Double = normedAxis.y
    val w: Double = normedAxis.z
    val xPrime = u * (u * x + v * y + w * z) * (1.0 - cos(theta)) + x * cos(theta) + (-w * y + v * z) * sin(theta)
    val yPrime = v * (u * x + v * y + w * z) * (1.0 - cos(theta)) + y * cos(theta) + (w * x - u * z) * sin(theta)
    val zPrime = w * (u * x + v * y + w * z) * (1.0 - cos(theta)) + z * cos(theta) + (-v * x + u * y) * sin(theta)
    return Vec3d(xPrime, yPrime, zPrime)
}

object VecUtils {
    val xAxis = newVec3d(1.0)
    val yAxis = newVec3d(y = 1.0)
    val zAxis = newVec3d(z = 1.0)
    val unit = Vec3d(1.0, 1.0, 1.0)
}
