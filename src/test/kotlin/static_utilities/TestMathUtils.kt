package static_utilities

import net.barribob.maelstrom.static_utilities.*
import net.barribob.maelstrom.static_utilities.MathUtils.axisOffset
import net.barribob.maelstrom.static_utilities.MathUtils.buildBlockCircle
import net.barribob.maelstrom.static_utilities.MathUtils.roundedStep
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
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

    @Test
    fun testAxisOffsetX() {
        val forward = Vec3d(0.0, 1.0, 1.0)
        val offset = VecUtils.xAxis
        val expected = forward.normalize()

        assertVecEquals(expected, axisOffset(forward, offset))
    }

    @Test
    fun testAxisOffsetY() {
        val forward = Vec3d(0.0, 1.0, -1.0)
        val offset = VecUtils.yAxis
        val expected = Vec3d(0.0, 1.0, 1.0).normalize()

        assertVecEquals(expected, axisOffset(forward, offset))
    }

    @Test
    fun testAxisOffsetZ() {
        val forward = Vec3d(0.0, 1.0, -1.0)
        val offset = VecUtils.zAxis
        val expected = Vec3d(1.0, 0.0, 0.0)

        assertVecEquals(expected, axisOffset(forward, offset))
    }

    @Test
    fun testAxisOffset_WhenDirectionAlongYAxis() {
        val forward = VecUtils.yAxis
        val offset = VecUtils.unit
        val expected = Vec3d(.0, 1.0, .0)

        assertVecEquals(expected, axisOffset(forward, offset))
    }

    @Test
    fun facingSameDirection_WhenFacingSimilarDirections() {
        val direction1 = Vec3d(1.0, 2.0, 1.0)
        val direction2 = Vec3d(1.0, 1.0, 2.0)

        Assertions.assertTrue(MathUtils.facingSameDirection(direction1, direction2))
    }

    @Test
    fun facingSameDirection_WhenOrthogonal() {
        val direction1 = VecUtils.yAxis
        val direction2 = VecUtils.zAxis

        Assertions.assertFalse(MathUtils.facingSameDirection(direction1, direction2))
    }

    @Test
    fun facingSameDirection_WhenFacingOpposingDirections() {
        val direction1 = Vec3d(-1.0, -2.0, -1.0)
        val direction2 = VecUtils.unit

        Assertions.assertFalse(MathUtils.facingSameDirection(direction1, direction2))
    }

    @Test
    fun unsignedAngle_Right() {
        val direction1 = VecUtils.xAxis
        val direction2 = VecUtils.yAxis

        Assertions.assertEquals(90.0, direction1.unsignedAngle(direction2))
    }

    @Test
    fun unsignedAngle_SameVectors() {
        Assertions.assertEquals(0.0, VecUtils.unit.unsignedAngle(VecUtils.unit))
    }

    @Test
    fun unsignedAngle_OppositeVectors() {
        Assertions.assertEquals(180.0, VecUtils.unit.unsignedAngle(VecUtils.unit.negateServer()))
    }

    @Test
    fun directionToYaw_xAxis() {
        Assertions.assertEquals(0.0, MathUtils.directionToYaw(VecUtils.xAxis))
    }

    @Test
    fun directionToYaw_yAxis() {
        Assertions.assertEquals(0.0, MathUtils.directionToYaw(VecUtils.yAxis))
    }

    @Test
    fun directionToYaw_zAxis() {
        Assertions.assertEquals(90.0, MathUtils.directionToYaw(VecUtils.zAxis))
    }

    @Test
    fun directionToYaw_negativeZAxis() {
        Assertions.assertEquals(-90.0, MathUtils.directionToYaw(VecUtils.zAxis.negate()))
    }

    @Test
    fun directionToYaw_negativeXAxis() {
        Assertions.assertEquals(180.0, MathUtils.directionToYaw(VecUtils.xAxis.negate()))
    }

    @Test
    fun testConsecutiveSum() {
        val firstNumber = 1
        val secondNumber = 100

        Assertions.assertEquals(5050, MathUtils.consecutiveSum(firstNumber, secondNumber))
    }

    @Test
    fun roundedStep_RoundsValueUp() {
        val result = roundedStep(1f, listOf(0f, 2.0f))

        Assertions.assertEquals(2.0f, result)
    }

    @Test
    fun roundedStep_KeepsValueAtSameStep() {
        val result = roundedStep(2.0f, listOf(0f, 2.0f))

        Assertions.assertEquals(2.0f, result)
    }

    @Test
    fun roundedStep_ValueLargerThanLargest() {
        val result = roundedStep(3.0f, listOf(0f, 2.0f))

        Assertions.assertEquals(2.0f, result)
    }

    @Test
    fun roundedStep_Floor() {
        val result = roundedStep(1.0f, listOf(0f, 2.0f), true)

        Assertions.assertEquals(0f, result)
    }

    @Test
    fun roundedStep_Floor_ValueSmallerThanSmallest() {
        val result = roundedStep(-1.0f, listOf(0f, 2.0f), true)

        Assertions.assertEquals(0f, result)
    }

    @Test
    fun roundedStep_Floor_ValueLargerThanLargest() {
        val result = roundedStep(3.0f, listOf(0f, 2.0f), true)

        Assertions.assertEquals(2.0f, result)
    }

    @Test
    fun roundedStep_Floor_KeepsValueAtSameStep() {
        val result = roundedStep(2.0f, listOf(0f, 2.0f), true)

        Assertions.assertEquals(2.0f, result)
    }

    @Test
    fun testVectorCoerceAtLeast() {
        assertVecEquals(VecUtils.unit, Vec3d.ZERO.coerceAtLeast(VecUtils.unit))
    }

    @Test
    fun testVectorCoerceAtMost() {
        assertVecEquals(VecUtils.unit, Vec3d(2.0, 2.0, 2.0).coerceAtMost(VecUtils.unit))
    }

    @Test
    fun buildBlockCircle_Radius1() {
        val points = buildBlockCircle(1.0)

        Assertions.assertTrue(points.contains(Vec3d.ZERO))
        Assertions.assertTrue(points.contains(VecUtils.xAxis))
        Assertions.assertTrue(points.contains(VecUtils.zAxis))
        Assertions.assertTrue(points.contains(newVec3d(-1.0)))
        Assertions.assertTrue(points.contains(newVec3d(z = -1.0)))
    }

    @ParameterizedTest
    @CsvSource (
        "0.5f, 0.5f, 0.0f",
        "0.75f, 0.5f, 0.5f",
        "1.0f, 0.5f, 1.0f",
        "0.75f, 0.75f, 0.0f",
        "0.875f, 0.75f, 0.5f",
        "1.0f, 0.75f, 1.0f",
        "1.1f, 0.5f, 1.0f",
        "0.5f, 0.0f, 0.5f",
        "0.0f, 0.5f, 0.0f"
        )
    fun testRatioLerp(time: Float, ratio: Float, result: Float) {
        val actual = MathUtils.ratioLerp(time, ratio, 1f, 0f)
        Assertions.assertEquals(result, actual)
    }

    @Test
    fun getBlocksInLine_AscendingDistances() {
        val start = BlockPos(10, -10, 10)
        val end = BlockPos(3, 3, -3)

        val blocks = MathUtils.getBlocksInLine(start, end)

        val comparisons = blocks.map { it.getSquaredDistance(start) }.zipWithNext().map { it.first < it.second }
        for(comparison in comparisons) {
            Assertions.assertTrue(comparison)
        }
    }
}