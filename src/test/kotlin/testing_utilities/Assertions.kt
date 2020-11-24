package testing_utilities

import net.minecraft.util.math.Vec3d
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertAll

fun assertVecEquals(expected: Vec3d, actual: Vec3d, error: Double = 0.0001) = assertAll(
    { Assertions.assertEquals(expected.x, actual.x, error, "X") },
    { Assertions.assertEquals(expected.y, actual.y, error, "Y") },
    { Assertions.assertEquals(expected.z, actual.z, error, "Z")})