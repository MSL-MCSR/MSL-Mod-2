package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.network.packet.c2s.play.UpdateDifficultyC2SPacket;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UpdateDifficultyC2SPacket.class)
public abstract class UpdateDifficultyC2SPacketMixin {
    @Shadow
    private Difficulty difficulty;

    @Inject(method = "<init>(Lnet/minecraft/world/Difficulty;)V", at = @At("TAIL"))
    private void changeDifficultyMixin(Difficulty difficulty, CallbackInfo ci) {
        if (MSLMod.ooml() && this.difficulty.equals(Difficulty.PEACEFUL)) {
            this.difficulty = Difficulty.EASY;
        }
    }
}
