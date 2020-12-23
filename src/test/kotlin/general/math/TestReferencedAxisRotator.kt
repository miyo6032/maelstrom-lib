package general.math

import net.barribob.maelstrom.general.math.ReferencedAxisRotator
import net.barribob.maelstrom.static_utilities.VecUtils
import net.minecraft.util.math.Vec3d
import org.junit.jupiter.api.Test
import testing_utilities.assertVecEquals

class TestReferencedAxisRotator {
    @Test
    fun referenceRotation_ChangeInAxis() {
        val originalAxis = VecUtils.xAxis
        val newAxis = VecUtils.yAxis

        assertVecEquals(VecUtils.yAxis, ReferencedAxisRotator(originalAxis, newAxis).rotate(VecUtils.xAxis))
    }

    @Test
    fun referenceRotation_RotateByAngleBetweenTwoAxes() {
        val originalAxis = VecUtils.xAxis
        val newAxis = VecUtils.yAxis
        val originalVector = Vec3d(0.0, 1.0, 1.0)

        assertVecEquals(Vec3d(-1.0, 0.0, 1.0), ReferencedAxisRotator(originalAxis, newAxis).rotate(originalVector))
    }
}