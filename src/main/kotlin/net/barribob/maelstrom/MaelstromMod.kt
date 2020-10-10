package net.barribob.maelstrom

import net.barribob.maelstrom.animation.client.ClientAnimationWatcher
import net.barribob.maelstrom.animation.server.ServerAnimationWatcher
import net.barribob.maelstrom.config.ConfigManager
import net.barribob.maelstrom.general.ClientServerUtils
import net.barribob.maelstrom.general.EventScheduler
import net.barribob.maelstrom.mob.AIManager
import net.barribob.maelstrom.util.HoconConfigManager
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object MaelstromMod {
    const val MODID = "maelstrom"
    val START_ANIMATION_PACKET_ID = Identifier(MODID, "start_animation")

    @Environment(EnvType.SERVER)
    val serverEventScheduler = EventScheduler()

    @Environment(EnvType.SERVER)
    val aiManager = AIManager()

    @Environment(EnvType.CLIENT)
    val clientAnimationWatcher = ClientAnimationWatcher()

    @Environment(EnvType.SERVER)
    val serverAnimationWatcher = ServerAnimationWatcher()

    @Environment(EnvType.SERVER)
    val configManager = ConfigManager()

    @Environment(EnvType.SERVER)
    val hoconConfigManager = HoconConfigManager()

    val LOGGER: Logger = LogManager.getLogger()
}

@Suppress("unused")
fun init() {
    ServerTickEvents.START_SERVER_TICK.register(ServerTickEvents.StartTick { MaelstromMod.serverEventScheduler.updateEvents() })

    MaelstromMod.LOGGER.info(MaelstromMod.hoconConfigManager.handleConfigLoad(MaelstromMod.MODID, "test").getString("test"))
}

@Environment(EnvType.CLIENT)
@Suppress("unused")
fun clientInit() {
    ClientSidePacketRegistry.INSTANCE.register(MaelstromMod.START_ANIMATION_PACKET_ID) { packetContext, packetData ->
        ClientServerUtils.startAnimationClient(
            packetContext,
            packetData
        )
    }
}