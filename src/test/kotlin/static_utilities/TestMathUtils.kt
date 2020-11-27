package static_utilities

import net.barribob.maelstrom.static_utilities.*
import net.minecraft.util.math.Vec3d
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import testing_utilities.assertVecEquals

class TestMathUtils {
    @Test
    fun withinDistance_WhenDistancesGreater_ReturnsFalse() {
        val result = MathUtils.withinDistance(Vec3d.ZERO, VecUtils.yAxis, 0.5)

        Assertions.assertFalse(result)
    }

    @Test
    fun withinDistance_WhenDistancesLess_ReturnsTrue() {
        val result = MathUtils.withinDistance(Vec3d.ZERO, VecUtils.zAxis, 1.5)

        Assertions.assertTrue(result)
    }

    @Test
    fun withinDistance_WhenDistanceNegative_ThrowsError() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            MathUtils.withinDistance(
                Vec3d.ZERO,
                VecUtils.zAxis,
                -1.0
            )
        }
    }

    @Test
    fun movingTowards_WhenDirectionPointsToCenter_ReturnsTrue() {
        val result = MathUtils.movingTowards(Vec3d.ZERO, VecUtils.yAxis, VecUtils.yAxis.negate())

        Assertions.assertTrue(result)
    }

    @Test
    fun movingTowards_WhenDirectionPointsAwayFromCenter_ReturnsFalse() {
        val result = MathUtils.movingTowards(Vec3d.ZERO, VecUtils.zAxis, VecUtils.zAxis)

        Assertions.assertFalse(result)
    }

    @Test
    fun movingTowards_WhenDirectionOrthogonal_ReturnsFalse() {
        val result = MathUtils.movingTowards(Vec3d.ZERO, VecUtils.zAxis, VecUtils.xAxis)

        Assertions.assertFalse(result)
    }

    @Test
    fun movingTowards_WhenNoDirection_ReturnsFalse() {
        val result = MathUtils.movingTowards(Vec3d.ZERO, VecUtils.zAxis, Vec3d.ZERO)

        Assertions.assertFalse(result)
    }

    @Test
    fun testUnNormedDirection() {
        val result = MathUtils.unNormedDirection(VecUtils.zAxis, VecUtils.xAxis)

        assertVecEquals(Vec3d(1.0, 0.0, -1.0), result)
    }

    @Test
    fun testPlaneProject() {
        val planeVector = Vec3d(0.0, 1.0, 0.0)
        val projection = newVec3d(1.0, 1.0, 1.0).planeProject(planeVector)
        Assertions.assertEquals(newVec3d(1.0, 0.0, 1.0), projection)
    }

    @Test
    fun rotateVector_WorksWithAxes() {
        val vec3d = VecUtils.zAxis
        val axis = VecUtils.yAxis
        val result = vec3d.rotateVector(axis, 90.0)
        val expected = VecUtils.xAxis

        assertVecEquals(expected, result)
    }

    @Test
    fun rotateVector_WorksOnNonAxisVectors() {
        val vec3d = VecUtils.unit
        val axis = VecUtils.yAxis
        val result = vec3d.rotateVector(axis, 90.0)
        val expected = Vec3d(1.0, 1.0, -1.0)

        assertVecEquals(expected, result)
    }

    @Test
    fun rotateVector_MaintainsVectorMagnitude() {
        val vec3d = VecUtils.unit.multiply(1.5)
        val axis = VecUtils.yAxis.multiply(0.5)
        val result = vec3d.rotateVector(axis, 90.0)

        val error = 0.0000001
        Assertions.assertEquals(vec3d.lengthSquared(), result.lengthSquared(), error)
    }

    @Test
    fun testNormalize() {
        val vec = newVec3d(2.0)

        val result = vec.normalize()
        val expected = VecUtils.xAxis

        assertVecEquals(expected, result)
    }

    /**
     * Normalization is actually very efficient for what we need -
     * It is that initializing minecraft MathHelper take a lot of
     * overhead.
     */
    @Test
    fun testNormalizeEfficiency() {
        val vecs = mutableListOf<Vec3d>()
        val intRange = 0 until 10000
        val randomVecs = intRange.map { RandomUtils.randVec().multiply(3.0) }
        val startTime = System.nanoTime()

        for (i in intRange) {
            vecs.add(randomVecs[i].normalize())
        }

        val stopTime = System.nanoTime()
        println((stopTime - startTime) * 1e-6)
        println(vecs.count())
    }

    @Test
    fun testNegateServer() {
        assertVecEquals(VecUtils.unit.negateServer(), Vec3d(-1.0, -1.0, -1.0))
    }

    @Test
    fun directionToPitchYaw_Forward_IsZeroPitch() {
        val pitch = MathUtils.directionToPitch(VecUtils.xAxis)

        Assertions.assertEquals(0.0f, pitch, 0.00001f)
    }

    @Test
    fun directionToPitchYaw_Up_IsNegativePitch() {
        val pitch = MathUtils.directionToPitch(VecUtils.yAxis)

        Assertions.assertEquals(-90.0f, pitch, 0.00001f)
    }

    @Test
    fun directionToPitchYaw_Down_IsPositivePitch() {
        val pitch = MathUtils.directionToPitch(VecUtils.yAxis.negateServer())

        Assertions.assertEquals(90.0f, pitch, 0.00001f)
    }

    @Test
    fun directionToPitchYaw_NegativeNonAxis_HasCorrectPitch() {
        val pitch = MathUtils.directionToPitch(Vec3d(0.0, -1.0, 1.0))

        Assertions.assertEquals(45.0f, pitch, 0.00001f)
    }

    @Test
    fun testLerpVec() {
        val result = MathUtils.lerpVec(0.1f, Vec3d.ZERO, VecUtils.yAxis)

        assertVecEquals(Vec3d(0.0, 0.1, 0.0), result)
    }
}