package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow
    private boolean seenCredits;

    @Shadow
    public abstract ServerWorld getServerWorld();

    @Inject(method = "changeDimension", at = @At("HEAD"))
    private void onSpeedrunEndMixin(ServerWorld destination, CallbackInfoReturnable<Entity> info) {
        ServerWorld serverWorld = getServerWorld();
        RegistryKey<World> registryKey = serverWorld.getRegistryKey();
        if (MSLMod.ooml() && registryKey.equals(World.END) && destination.getRegistryKey().equals(World.OVERWORLD) && !seenCredits) {
            MSLMod.eo().complete((ServerPlayerEntity) (Object) this);
        }

    }
}
