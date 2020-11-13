package net.barribob.maelstrom.static_utilities

import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.barribob.maelstrom.general.io.ILogger
import net.barribob.maelstrom.general.io.config.IConfig
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringNbtReader

object NbtUtils {
    fun readDefaultNbt(logger: ILogger, config: IConfig): CompoundTag {
        return try {
            StringNbtReader.parse(config.toJsonString())
        } catch (e: CommandSyntaxException) {
            logger.error("Error parsing nbt for text: $config")
            CompoundTag()
        }
    }
}