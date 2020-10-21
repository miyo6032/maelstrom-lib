package net.barribob.maelstrom.static_utilities

import net.minecraft.network.PacketByteBuf

fun PacketByteBuf.readFloatList(count: Int): List<Float> {
    if (count < 0) throw IllegalArgumentException("Count should be greater than zero")
    return (0 until count).map { this.readFloat() }
}

fun PacketByteBuf.writeFloatList(list: List<Float>) = list.forEach { this.writeFloat(it) }