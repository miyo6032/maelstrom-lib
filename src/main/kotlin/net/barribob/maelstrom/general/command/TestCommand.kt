package net.barribob.maelstrom.general.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.barribob.maelstrom.MaelstromMod
import net.barribob.maelstrom.general.event.TimedEvent
import net.barribob.maelstrom.static_utilities.InGameTests
import net.barribob.maelstrom.static_utilities.format
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.CommandSource
import net.minecraft.command.suggestion.SuggestionProviders
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import java.util.*
import kotlin.system.measureNanoTime

class TestCommand : CommandRegistrationCallback {
    private val notFoundException = DynamicCommandExceptionType { LiteralText("Test name not found") }

    private val tests = mutableMapOf<Identifier, (ServerCommandSource) -> Unit>()

    private val nameArgumentName = "name"

    init {
        addId(InGameTests::lineCallback.name, InGameTests::lineCallback)
        addId(InGameTests::boxCorners.name, InGameTests::boxCorners)
        addId(InGameTests::willBoxFit.name, InGameTests::willBoxFit)
        addId(InGameTests::raycast.name, InGameTests::raycast)
    }

    private val suggestions: SuggestionProvider<ServerCommandSource> =
        SuggestionProviders.register<ServerCommandSource>(
            Identifier(MaelstromMod.MODID, "test"),
            SuggestionProvider<CommandSource> { _, builder ->
                CommandSource.forEachMatching(
                    tests.keys,
                    builder.remaining.toLowerCase(Locale.ROOT),
                    { it },
                    { builder.suggest(it.toString()) })
                return@SuggestionProvider builder.buildFuture()
            })

    private fun addId(
        name: String,
        callback: (ServerCommandSource) -> Unit
    ) = tests.put(Identifier(MaelstromMod.MODID, name.toLowerCase(Locale.ROOT)), callback)

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>, p1: Boolean) {
        val commandName = "libtest"
        val timeArgumentName = "ticks"
        dispatcher.register(
            CommandManager.literal(commandName).then(
                CommandManager.argument(nameArgumentName, TestArgumentType(this))
                    .suggests(suggestions)
                    .executes { run(it) }
                    .then(
                        CommandManager.argument(timeArgumentName, IntegerArgumentType.integer(1))
                            .executes { run(it, IntegerArgumentType.getInteger(it, timeArgumentName)) }
                    )
            )
        )
    }

    private fun run(context: CommandContext<ServerCommandSource>, ticks: Int = 1): Int {
        val identifier = context.getArgument(nameArgumentName, Identifier::class.java)
        validate(identifier)
        var time = 0L

        val runTest: () -> Unit = {
            try {
                time += measureNanoTime { tests[identifier]?.invoke(context.source) }
            } catch (e: Exception) {
                context.source.sendFeedback(LiteralText(e.message), false)
                e.printStackTrace()
            }
        }

        MaelstromMod.serverEventScheduler.addEvent(TimedEvent(runTest, 0, ticks))
        MaelstromMod.serverEventScheduler.addEvent(TimedEvent({
            context.source.sendFeedback(
                LiteralText("Test(s) ran using ${((time / ticks) * 1e-6).format(3)} ms of runtime"),
                false
            )
        }, ticks))

        return 1
    }

    fun validate(identifier: Identifier) {
        if (!tests.containsKey(identifier)) {
            throw notFoundException.create(identifier)
        }
    }
}