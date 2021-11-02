package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ServerResourceManager.class)
public abstract class ServerResourceManagerMixin {
    @Inject(method = "reload", at = @At("TAIL"))
    private static void checkDatapacksMixin(List<ResourcePack> list, CommandManager.RegistrationEnvironment registrationEnvironment, int i, Executor executor, Executor executor2, CallbackInfoReturnable<CompletableFuture<ServerResourceManager>> info) {
        try {
            if (MSLMod.ooml()) {
                MSLMod.eo().checkDatapacks(Objects.requireNonNull(MinecraftClient.getInstance().getServer()));
            }
        } catch (Exception ignored) {
        }
    }
}
