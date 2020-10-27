package net.barribob.maelstrom.static_utilities

import com.mojang.blaze3d.systems.RenderSystem
import net.barribob.maelstrom.render.RenderData
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.Tessellator
import net.minecraft.client.render.VertexFormats
import org.lwjgl.opengl.GL11

object RenderUtils {
    fun renderPoints(renderData: RenderData, color: List<Float>, points: List<Float>) {
        RenderSystem.pushMatrix()
        RenderSystem.multMatrix(renderData.matrixStack.peek().model)
        RenderSystem.enableBlend()
        RenderSystem.defaultAlphaFunc()
        RenderSystem.lineWidth(2.0f)
        GL11.glPointSize(2.0f)
        RenderSystem.disableTexture()
        RenderSystem.depthMask(false)
        RenderSystem.translated(-renderData.camera.pos.x, -renderData.camera.pos.y, -renderData.camera.pos.z)
        val tessellate: Tessellator = Tessellator.getInstance()
        val bufferBuilder: BufferBuilder = tessellate.buffer
        bufferBuilder.begin(GL11.GL_POINTS, VertexFormats.POSITION_COLOR)

        (points.indices step 3).forEach {
            bufferBuilder
                .vertex(points[it].toDouble(), points[it + 1].toDouble(), points[it + 2].toDouble())
                .color(color[0], color[1], color[2], color[3]).next()
        }

        tessellate.draw()

        RenderSystem.depthMask(true)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
        RenderSystem.popMatrix()
    }
}