package net.barribob.maelstrom

import net.barribob.maelstrom.general.ConfigManager
import net.barribob.maelstrom.general.command.TestArgumentType
import net.barribob.maelstrom.general.command.TestCommand
import net.barribob.maelstrom.general.event.EventScheduler
import net.barribob.maelstrom.general.io.*
import net.barribob.maelstrom.mob.AIManager
import net.barribob.maelstrom.render.RenderMap
import net.barribob.maelstrom.static_utilities.ClientServerUtils
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.command.argument.ArgumentTypes
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object MaelstromMod {
    // Todo: refactor this to separate server from client
    const val MODID = "maelstrom"
    val DRAW_POINTS_PACKET_ID = Identifier(MODID, "draw_points")

    @Deprecated("Causes unknown behavior across worlds")
    @Environment(EnvType.SERVER)
    val serverEventScheduler = EventScheduler()

    @Environment(EnvType.SERVER)
    val aiManager = AIManager()

    @Environment(EnvType.SERVER)
    val configManager = ConfigManager()

    val LOGGER: ILogger = ConsoleLogger(LogManager.getLogger())

    @Environment(EnvType.CLIENT)
    val renderMap = RenderMap()

    @Deprecated("Causes unknown behavior across worlds")
    @Environment(EnvType.CLIENT)
    val clientEventScheduler = EventScheduler()

    val testCommand = TestCommand()
}

@Suppress("unused")
fun init() {
    ServerTickEvents.START_SERVER_TICK.register(ServerTickEvents.StartTick { MaelstromMod.serverEventScheduler.updateEvents() })

    CommandRegistrationCallback.EVENT.register(MaelstromMod.testCommand)
    ArgumentTypes.register(
        "${MaelstromMod.MODID}:libtest",
        TestArgumentType::class.java,
        ConstantArgumentSerializer { TestArgumentType(MaelstromMod.testCommand) })
}

@Environment(EnvType.CLIENT)
@Suppress("unused")
fun clientInit() {
    ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvents.StartTick { MaelstromMod.clientEventScheduler.updateEvents() })
    ClientSidePacketRegistry.INSTANCE.register(MaelstromMod.DRAW_POINTS_PACKET_ID) { packetContext, packetData ->
        ClientServerUtils.drawDebugPointsClient(packetContext, packetData)
    }
}