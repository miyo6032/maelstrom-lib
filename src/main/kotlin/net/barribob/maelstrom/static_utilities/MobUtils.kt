package net.barribob.maelstrom.static_utilities

import net.barribob.maelstrom.mob.ai.BlockType
import net.minecraft.block.*
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.FollowTargetGoal
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.ai.goal.RevengeGoal
import net.minecraft.entity.ai.pathing.NavigationType
import net.minecraft.entity.boss.dragon.EnderDragonPart
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.tag.BlockTags
import net.minecraft.tag.FluidTags
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import net.minecraft.world.World
import kotlin.math.min
import kotlin.math.pow

fun Entity.addVelocity(vec: Vec3d) {
    this.addVelocity(vec.x, vec.y, vec.z)
}

fun Entity.setPos(vec: Vec3d) {
    this.updatePosition(vec.x, vec.y, vec.z)
}

fun Entity.eyePos(): Vec3d = this.getCameraPosVec(0f)

val Entity.registryId: String
    get() = Registry.ENTITY_TYPE.getId(type).toString()

/**
 * Static utility functions that use or depend [Entity]
 */
object MobUtils {
    @Deprecated("Inflexible and untested")
    fun leapTowards(entity: LivingEntity, target: Vec3d, horzVel: Double, yVel: Double) {
        val dir = target.subtract(entity.pos).normalize()
        val leap: Vec3d = Vec3d(dir.x, 0.0, dir.z).normalize().multiply(horzVel).yOffset(yVel)
        val clampedYVelocity = if (entity.velocity.y < 0.1) leap.y else 0.0

        // Normalize to make sure the velocity doesn't go beyond what we expect
        var horzVelocity = entity.velocity.add(leap.x, 0.0, leap.z)
        val scale = horzVel / horzVelocity.length()
        if (scale < 1) {
            horzVelocity = horzVelocity.multiply(scale)
        }

        entity.velocity = horzVelocity.yOffset(clampedYVelocity)
    }

    fun getBlockType(world: BlockView, pos: BlockPos, callsLeft: Int): BlockType {
        val blockState = world.getBlockState(pos)
        val block = blockState.block
        val material = blockState.material
        val fluidState = world.getFluidState(pos)
        val belowType = if(pos.y > 0 && callsLeft > 0) getBlockType(world, pos.down(), callsLeft - 1) else BlockType.OPEN

        return when {
            blockState.isOf(Blocks.SWEET_BERRY_BUSH) ||
                    blockState.isIn(BlockTags.FIRE) ||
                    CampfireBlock.isLitCampfire(blockState) ||
                    fluidState.isIn(FluidTags.WATER) -> BlockType.PASSABLE_OBSTACLE
            fluidState.isIn(FluidTags.LAVA) ||
                    blockState.isOf(Blocks.CACTUS) ||
                    blockState.isOf(Blocks.HONEY_BLOCK) ||
                    blockState.isOf(Blocks.MAGMA_BLOCK) -> BlockType.SOLID_OBSTACLE
            block is LeavesBlock ||
                    block.isIn(BlockTags.FENCES) ||
                    block.isIn(BlockTags.WALLS) ||
                    (block is FenceGateBlock && !blockState.get(FenceGateBlock.OPEN)) ||
                    (DoorBlock.isWoodenDoor(blockState) && !blockState.get(DoorBlock.OPEN)) ||
                    (block is DoorBlock && material == Material.METAL && !blockState.get(DoorBlock.OPEN)) ||
                    (block is DoorBlock && blockState.get(DoorBlock.OPEN)) ||
                    !blockState.canPathfindThrough(world, pos, NavigationType.LAND) -> BlockType.BLOCKED
            belowType == BlockType.BLOCKED -> BlockType.WALKABLE
            belowType == BlockType.OPEN -> BlockType.PASSABLE_OBSTACLE
            belowType == BlockType.PASSABLE_OBSTACLE -> BlockType.PASSABLE_OBSTACLE
            belowType == BlockType.SOLID_OBSTACLE -> BlockType.PASSABLE_OBSTACLE
            else -> BlockType.OPEN
        }
    }

    @Deprecated("Untested and specific to jumping ai")
    fun getJumpVelocity(world: World, entity: LivingEntity): Double {
        var baseVelocity = 0.42 * getJumpVelocityMultiplier(world, entity)
        if (entity.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            baseVelocity += 0.1 * (entity.getStatusEffect(StatusEffects.JUMP_BOOST)!!.amplifier + 1)
        }
        return baseVelocity
    }

    @Deprecated("Untested and specific to jumping ai")
    private fun getJumpVelocityMultiplier(world: World, entity: LivingEntity): Double {
        val f: Float = world.getBlockState(entity.blockPos).block.jumpVelocityMultiplier
        val g: Float = world.getBlockState(getVelocityAffectingPos(entity)).block.jumpVelocityMultiplier
        return if (f.toDouble() == 1.0) g.toDouble() else f.toDouble()
    }

    @Deprecated("Untested and specific to jumping ai")
    private fun getVelocityAffectingPos(entity: LivingEntity): BlockPos? {
        return BlockPos(entity.pos.x, entity.boundingBox.minY - 0.5000001, entity.pos.z)
    }
}