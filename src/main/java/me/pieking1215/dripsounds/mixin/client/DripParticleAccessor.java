package me.pieking1215.dripsounds.mixin.client;

import net.minecraft.client.particle.DripParticle;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DripParticle.class)
public interface DripParticleAccessor {
    @Accessor("type")
    Fluid dripsounds$type();
}
