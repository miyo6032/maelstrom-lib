package general.event

import net.barribob.maelstrom.general.IdProviders
import net.barribob.maelstrom.general.IdRegistry
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestIdProvider {
    @Test
    fun registryRegisters() {
        val registryName = "key"
        val registryObject = "toBeRegistered"
        val registry = IdRegistry<String>(IdProviders.INCREMENT)
        val id = registry.register(registryName, registryObject)

        assertEquals(id, registry.getIdFromName(registryName))
        assertEquals(registryName, registry.getNameFromId(id))
        assertEquals(registryObject, registry.get(registryName))
    }

    @Test
    fun incrementId() {
        val ids = listOf(0, 1, 3)
        assertEquals(4, IdProviders.INCREMENT(ids))
    }
}