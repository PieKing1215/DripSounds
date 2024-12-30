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
}
