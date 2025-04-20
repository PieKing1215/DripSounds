package me.pieking1215.dripsounds.mixin.client;

import me.pieking1215.dripsounds.DripSoundsConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientLevel.class)
public class MixinClientLevel {
    @ModifyConstant(method = "doAnimateTick", constant = @Constant(intValue = 10))
    private int modifyDripChance(int original){
        return DripSoundsConfig.GENERAL.dripChance.get();
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 667))
    private int modifyAnimateCount(int original){
        return (int)(original * DripSoundsConfig.GENERAL.blockAnimateCountMultiplier.get());
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 16))
    private int modifyAnimateRange1(int original){
        return (int)(original * DripSoundsConfig.GENERAL.blockAnimateRangeMultiplier.get());
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 32))
    private int modifyAnimateRange2(int original){
        return (int)(original * DripSoundsConfig.GENERAL.blockAnimateRangeMultiplier.get());
    }
}
