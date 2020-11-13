package net.barribob.maelstrom.general.io.config

interface IConfig {
    fun hasPath(path: String): Boolean
    fun isEmpty(): Boolean
    fun getBoolean(path: String): Boolean
    fun getNumber(path: String): Number
    fun getInt(path: String): Int
    fun getLong(path: String): Long
    fun getDouble(path: String): Double
    fun getFloat(path: String): Float
    fun getString(path: String): String
    fun getConfig(path: String): IConfig
    fun getBooleanList(path: String): List<Boolean>
    fun getNumberList(path: String): List<Number>
    fun getIntList(path: String): List<Int>
    fun getLongList(path: String): List<Long>
    fun getDoubleList(path: String): List<Double>
    fun getStringList(path: String): List<String>
    fun getConfigList(path: String): List<IConfig>
    fun toJsonString(): String
}