package general.data

import net.barribob.maelstrom.general.data.WeakHashPredicate
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestWeakHashPredicate {
    @Test
    fun maintainsReferenceAcrossMultipleCalls() {
        var referencesGenerated = 0
        val factory = {
            referencesGenerated++
            { true }
        }
        val predicate = WeakHashPredicate<String>(factory)

        val ref = ""
        predicate.test(ref)
        predicate.test(ref)

        Assertions.assertEquals(1, referencesGenerated)
    }
}