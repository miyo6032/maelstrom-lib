package net.barribob.maelstrom.static_utilities

import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.typesafe.config.Config
import com.typesafe.config.ConfigRenderOptions
import net.barribob.maelstrom.general.io.ILogger
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.StringNbtReader

object NbtUtils {
    fun readDefaultNbt(logger: ILogger, config: Config): CompoundTag {
        return try {
            StringNbtReader.parse(
                config.root().render(
                    ConfigRenderOptions.concise()
                )
            )
        } catch (e: CommandSyntaxException) {
            logger.error("Error parsing nbt for text: $config")
            CompoundTag()
        }
    }
}