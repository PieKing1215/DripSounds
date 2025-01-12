package me.pieking1215.dripsounds.mixin.client;

import me.pieking1215.dripsounds.DripSoundsConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DripParticle.FallAndLandParticle.class)
public class MixinFallAndLandParticle {
    @Inject(
            method = "postMoveUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/DripParticle$FallAndLandParticle;remove()V")
    )
    private void onLand(CallbackInfo ci) {
        // if mod is enabled in the config
        if(DripSoundsConfig.GENERAL.enabled.get()){
            DripParticle.FallAndLandParticle particle = (DripParticle.FallAndLandParticle)(Object)this;
            ClientLevel level = particle.level;
            double x = particle.x;
            double y = particle.y;
            double z = particle.z;
            Fluid particleFluid = particle.type;
            FluidState belowFluid = level.getBlockState(new BlockPos((int)x, (int)y - 1, (int)z)).getFluidState();

            SoundEvent play = null;
            float volumeMod = 1f;
            float pitch = 1f;

            // if particle is lava, only play sound if landed on block or non-lava fluid (water)
            // if particle is water, only play sound if landed on block
            if (particleFluid == Fluids.LAVA && belowFluid.getType() != Fluids.LAVA) {
                play = SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA;
            } else if (particleFluid == Fluids.WATER && belowFluid.isEmpty()) {
                play = SoundEvents.POINTED_DRIPSTONE_DRIP_WATER;
            }

            if (play != null) {
                // play the sound
                float vol = Mth.clamp(DripSoundsConfig.GENERAL.volume.floatValue() * volumeMod, 0f, 1f);
                if(DripSoundsConfig.GENERAL.useDripstoneSounds.get()) {
                    vol *= Math.random() * 0.7 + 0.3; // same as vanilla dripstone drips
                }
                level.playLocalSound(x, y, z, play, DripSoundsConfig.GENERAL.soundCategory.get(), vol, pitch, false);
            }
        }
    }
}
