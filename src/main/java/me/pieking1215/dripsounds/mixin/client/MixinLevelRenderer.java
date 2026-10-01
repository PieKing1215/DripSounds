package me.pieking1215.dripsounds.mixin.client;

import me.pieking1215.dripsounds.DripSoundsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//? if >= 1.21.9 {

@Mixin(net.minecraft.client.multiplayer.ClientLevel.class)
public class MixinLevelRenderer {
    @ModifyConstant(
            method = "doAddParticle",
            constant = @Constant(doubleValue = 1024.0),
            require = 0
    )
    private double modifyMaxParticleAddRange(double original){
        return Math.max(original, original * DripSoundsConfig.GENERAL.blockAnimateRangeMultiplier.get());
    }
}

//?} else {

/*@Mixin(net.minecraft.client.renderer.LevelRenderer.class)
public class MixinLevelRenderer {
    @ModifyConstant(
        method = "Lnet/minecraft/client/renderer/LevelRenderer;addParticleInternal(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)Lnet/minecraft/client/particle/Particle;",
        constant = @Constant(doubleValue = 1024.0),
        require = 0
    )
    private double modifyMaxParticleAddRange(double original){
        return Math.max(original, original * DripSoundsConfig.GENERAL.blockAnimateRangeMultiplier.get());
    }
}

*///?}