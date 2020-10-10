package net.barribob.maelstrom.animation.client

import net.minecraft.entity.Entity
import software.bernie.geckolib.entity.IAnimatedEntity

interface IAnimationRegister<T> where T : Entity, T : IAnimatedEntity {
    fun registerAnimations(animationManager: GeckolibAnimationManager<T>)
}