package com.mcsrleague.mslmod.mixin.game;

import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin {
    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    private void noTridentsMixin(LocalDifficulty difficulty, CallbackInfo info) {
        info.cancel();
    }
}
