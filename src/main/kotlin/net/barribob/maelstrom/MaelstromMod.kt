package net.barribob.maelstrom

import net.barribob.maelstrom.MaelstromMod.isDevelopmentEnvironment
import net.barribob.maelstrom.general.command.TestArgumentType
import net.barribob.maelstrom.general.command.TestCommand
import net.barribob.maelstrom.general.event.EventScheduler
import net.barribob.maelstrom.general.io.ConsoleLogger
import net.barribob.maelstrom.general.io.ILogger
import net.barribob.maelstrom.mixin.ArgumentTypeAccessor
import net.barribob.maelstrom.render.RenderMap
import net.barribob.maelstrom.static_utilities.DebugPointsNetworkHandler
import net.barribob.maelstrom.static_utilities.InGameTests
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager

object MaelstromMod {
    internal const val MODID = "maelstrom"
    internal val DRAW_POINTS_PACKET_ID = Identifier(MODID, "draw_points")

    val renderMap = RenderMap()

    @Deprecated("Causes unknown behavior across worlds")
    internal val clientEventScheduler = EventScheduler()

    val LOGGER: ILogger = ConsoleLogger(LogManager.getLogger())
    val debugPoints = DebugPointsNetworkHandler()
    val testCommand = TestCommand(InGameTests(debugPoints))

    val isDevelopmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment
}

fun init() {
    if(isDevelopmentEnvironment){
        initDev()
    }
}

private fun initDev() {
    CommandRegistrationCallback.EVENT.register(MaelstromMod.testCommand)
    ArgumentTypeAccessor.register(Registry.COMMAND_ARGUMENT_TYPE,
        "${MaelstromMod.MODID}:libtest",
        TestArgumentType::class.java,
        ConstantArgumentSerializer.of { _ -> TestArgumentType(MaelstromMod.testCommand) })
}