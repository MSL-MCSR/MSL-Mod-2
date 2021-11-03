package com.mcsrleague.mslmod.mixin.game;

import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin implements ServerPlayPacketListener {
    @Shadow private int messageCooldown;

    @Inject(method = "onGameMessage",at=@At("HEAD"))
    public void preventSpamKickMixin(ChatMessageC2SPacket packet, CallbackInfo ci) {
        this.messageCooldown = 0;
    }
}
