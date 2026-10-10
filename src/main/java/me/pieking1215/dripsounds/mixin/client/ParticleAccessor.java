package me.pieking1215.dripsounds.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {
    @Accessor("level")
    ClientLevel dripsounds$level();
    @Accessor("x")
    double dripsounds$x();
    @Accessor("y")
    double dripsounds$y();
    @Accessor("z")
    double dripsounds$z();
}
