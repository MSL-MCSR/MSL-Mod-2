package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.random.SpeedrunRandomHelper;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EyeOfEnderEntity.class)
public abstract class EyeOfEnderEntityMixin {
    @Shadow
    private boolean dropsItem;

    @Inject(method = "moveTowards", at = @At("TAIL"))
    private void eyeBreakOverrideMixin(BlockPos pos, CallbackInfo info) {
        dropsItem = SpeedrunRandomHelper.eyeRandom.nextInt(5) > 0;
    }
}
