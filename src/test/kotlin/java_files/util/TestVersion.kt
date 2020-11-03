package java_files.util

import net.barribob.maelstrom.util.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import java.lang.IllegalArgumentException

class TestVersion {
    @Test
    fun stringVersionThrowsException() {
        assertThrows(IllegalArgumentException::class.java, { Version("1.0rc") }, "Invalid version format")
    }

    @Test
    fun specialCharactersThrowsException() {
        assertThrows(IllegalArgumentException::class.java, { Version("1/0") }, "Invalid version format")
    }

    @Test
    fun negativeVersionsThrowException() {
        assertThrows(IllegalArgumentException::class.java, { Version("-1") }, "Invalid version format")
    }

    @Test
    fun canCreateVersionWithMultipleDigits() {
        Version("1.0.12.7")
    }

    @Test
    fun longerVersionOfSameVersionIsLarger() {
        val v1 = Version("1")
        val v2 = Version("1.2")
        assertEquals(-1, v1.compareTo(v2))
    }

    @Test
    fun lowerVersionIsSmaller() {
        val v1 = Version("1")
        val v2 = Version("2")
        assertEquals(-1, v1.compareTo(v2))
    }

    @Test
    fun versionsCompareMajorVersionFirst() {
        val v1 = Version("1.2")
        val v2 = Version("2.1")
        assertEquals(-1, v1.compareTo(v2))
    }

    @Test
    fun sameVersionsComparedConsideredEqual() {
        val v1 = Version("1.12.2")
        val v2 = Version("1.12.2")
        assertEquals(0, v1.compareTo(v2))
    }

    @Test
    fun nullVersionSmaller() {
        val v1 = Version("1.12.2")
        val v2 = null
        assertEquals(1, v1.compareTo(v2))
    }
}