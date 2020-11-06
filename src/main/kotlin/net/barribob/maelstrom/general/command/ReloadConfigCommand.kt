package net.barribob.maelstrom.general.command

import com.mojang.brigadier.CommandDispatcher
import net.barribob.maelstrom.MaelstromMod
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class ReloadConfigCommand : CommandRegistrationCallback {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>, p1: Boolean) {
        val commandName = "reloadConf"
        dispatcher.register(
            CommandManager.literal(commandName).executes {
                MaelstromMod.configRegistry.reloadConfigs()
                return@executes 1
             }
        )
    }
}