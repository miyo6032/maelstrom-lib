package net.barribob.maelstrom

import net.barribob.maelstrom.general.ConfigManager
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
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager

object MaelstromMod {
    // Todo: refactor this to separate server from client
    const val MODID = "maelstrom"
    val DRAW_POINTS_PACKET_ID = Identifier(MODID, "draw_points")

    @Environment(EnvType.SERVER)
    val serverEventScheduler = EventScheduler()

    @Environment(EnvType.SERVER)
    val aiManager = AIManager()

    @Environment(EnvType.SERVER)
    val configManager = ConfigManager()

    val LOGGER: ILogger = ConsoleLogger(LogManager.getLogger())

    @Environment(EnvType.SERVER)
    val hoconConfigManager = VersionedConfigLoader(LOGGER, ConfigVersionFactory(), ModFileManager(LOGGER))

    @Environment(EnvType.CLIENT)
    val renderMap = RenderMap()

    @Environment(EnvType.CLIENT)
    val clientEventScheduler = EventScheduler()
}

@Suppress("unused")
fun init() {
    ServerTickEvents.START_SERVER_TICK.register(ServerTickEvents.StartTick { MaelstromMod.serverEventScheduler.updateEvents() })
    ClientTickEvents.START_CLIENT_TICK.register(ClientTickEvents.StartTick { MaelstromMod.clientEventScheduler.updateEvents() })

    CommandRegistrationCallback.EVENT.register(TestCommand())
}

@Environment(EnvType.CLIENT)
@Suppress("unused")
fun clientInit() {
    ClientSidePacketRegistry.INSTANCE.register(MaelstromMod.DRAW_POINTS_PACKET_ID) { packetContext, packetData ->
        ClientServerUtils.drawDebugPointsClient(packetContext, packetData) }
}