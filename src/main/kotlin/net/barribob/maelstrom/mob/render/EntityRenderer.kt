package net.barribob.maelstrom.mob.render

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.MobEntityRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.entity.mob.MobEntity
import net.minecraft.util.Identifier

/*
Entity renderer that allows any entity to use that doesn't need any special rendering capabilities
 */
@Environment(EnvType.CLIENT)
class EntityRenderer<T: MobEntity, M: EntityModel<T>>(entityRenderDispatcher: EntityRenderDispatcher, entityModel: M, shadowSize: Float, private val textureLocation: Identifier) : MobEntityRenderer<T, M>(entityRenderDispatcher, entityModel, shadowSize) {
    override fun getTexture(entity: T): Identifier = textureLocation
}