package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.WarningModsUtil;
import com.mcsrleague.mslmod.random.SpeedrunRandomHelper;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Inject(method = "createLevel", at = @At("HEAD"))
    private void createLevelMixin(CallbackInfo ci) {
        if (WarningModsUtil.isTm() && (!MSLMod.ooml()) && (!SpeedrunRandomHelper.hasOverride())) {
            SpeedrunRandomHelper.setOverride(487317577770548373L);
        }
    }
}