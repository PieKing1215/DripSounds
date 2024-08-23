package me.pieking1215.waterdripsound.mixin.client;

import me.pieking1215.waterdripsound.WaterDripSound;
import me.pieking1215.waterdripsound.WaterDripSoundConfig;
import net.minecraft.client.particle.BlockLeakParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.particle.BlockLeakParticle$ContinuousFalling")
public abstract class MixinContinuousFallingBlockLeakParticle extends SpriteBillboardParticle {
    protected MixinContinuousFallingBlockLeakParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/BlockLeakParticle$ContinuousFalling;markDead()V"), method = "updateVelocity")
    private void createParticle(CallbackInfo ci) {
        // if mod is enabled in the config
        if(WaterDripSoundConfig.GENERAL.enabled.get()){
            ClientWorld clientWorld = this.world;
            double x = this.x;
            double y = this.y;
            double z = this.z;
            BlockLeakParticle blp = (BlockLeakParticle)(SpriteBillboardParticle)this;
            Fluid particleFluid = ((BlockLeakParticleAccessor)blp).getFluid();
            FluidState belowFluid = clientWorld.getBlockState(new BlockPos((int)x, (int)y - 1, (int)z)).getFluidState();

            SoundEvent play = null;
            float volumeMod = 1f;
            float pitch = 1f;

            // if particle is lava, only play sound if landed on block or non-lava fluid (water)
            // if particle is water, only play sound if landed on block
            if (particleFluid == Fluids.LAVA && belowFluid.getFluid() != Fluids.LAVA) {
                play = WaterDripSoundConfig.GENERAL.useDripstoneSounds.get() ? SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_LAVA : WaterDripSound.LAVA_DRIP_EVENT;
                if (!WaterDripSoundConfig.GENERAL.useDripstoneSounds.get()) {
                    pitch = 1f + (float)(Math.random() * 0.1f);
                    volumeMod = 0.5f;
                }
            } else if (particleFluid == Fluids.WATER && belowFluid.isEmpty()) {
                play = WaterDripSoundConfig.GENERAL.useDripstoneSounds.get() ? SoundEvents.BLOCK_POINTED_DRIPSTONE_DRIP_WATER : SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP;
            }

            if (play != null) {
                // play the sound
                float vol = MathHelper.clamp(WaterDripSoundConfig.GENERAL.volume.floatValue() * volumeMod, 0f, 1f);
                if(WaterDripSoundConfig.GENERAL.useDripstoneSounds.get()) {
                    vol *= Math.random() * 0.7 + 0.3; // same as vanilla dripstone drips
                }
                clientWorld.playSound(x, y, z, play, WaterDripSoundConfig.GENERAL.soundCategory.get(), vol, pitch, false);
            }
        }
    }
}
