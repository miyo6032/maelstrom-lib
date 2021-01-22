package net.barribob.maelstrom.general

object IdProviders {
    val INCREMENT = { ids: Collection<Int> ->
        val maxId = ids.maxOrNull()
        if (maxId != null) maxId + 1 else 0
    }
}

class IdRegistry<T>(val idProvider: (Collection<Int>) -> Int) {
    private val idMap = hashMapOf<String, Int>()
    private val nameMap = hashMapOf<Int, String>()
    private val objects = hashMapOf<String, T>()

    fun register(name: String, obj: T): Int {
        val id = idProvider(idMap.values)
        idMap[name] = id
        nameMap[id] = name
        objects[name] = obj
        return id
    }

    fun getIdFromName(name: String): Int? = idMap[name]
    fun getNameFromId(id: Int): String? = nameMap[id]
    fun get(name: String): T? = objects[name]
}