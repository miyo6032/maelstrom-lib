package net.barribob.maelstrom.general.io.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigRenderOptions

class TypesafeConfig(private val typesafeConfig: Config) : IConfig {
    override fun hasPath(path: String): Boolean = typesafeConfig.hasPath(path)

    override fun isEmpty(): Boolean = typesafeConfig.isEmpty

    override fun getBoolean(path: String): Boolean = typesafeConfig.getBoolean(path)

    override fun getNumber(path: String): Number = typesafeConfig.getNumber(path)

    override fun getInt(path: String): Int = typesafeConfig.getInt(path)

    override fun getLong(path: String): Long = typesafeConfig.getLong(path)

    override fun getDouble(path: String): Double = typesafeConfig.getDouble(path)

    override fun getFloat(path: String): Float = typesafeConfig.getDouble(path).toFloat()

    override fun getString(path: String): String = typesafeConfig.getString(path)

    override fun getConfig(path: String): IConfig = TypesafeConfig(typesafeConfig.getConfig(path))

    override fun getBooleanList(path: String): List<Boolean> = typesafeConfig.getBooleanList(path)

    override fun getNumberList(path: String): List<Number> = typesafeConfig.getNumberList(path)

    override fun getIntList(path: String): List<Int> = typesafeConfig.getIntList(path)

    override fun getLongList(path: String): List<Long> = typesafeConfig.getLongList(path)

    override fun getDoubleList(path: String): List<Double> = typesafeConfig.getDoubleList(path)

    override fun getStringList(path: String): List<String> = typesafeConfig.getStringList(path)

    override fun getConfigList(path: String): List<IConfig> =
        typesafeConfig.getConfigList(path).map { TypesafeConfig(it) }

    override fun toJsonString(): String = typesafeConfig.root().render(ConfigRenderOptions.concise())
}