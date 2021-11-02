package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.random.SpeedrunRandomHelper;
import com.mcsrleague.mslmod.session.SessionWorld;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    private static final List<PhaseType<?>> perchPhases = Arrays.asList(
            PhaseType.LANDING_APPROACH,
            PhaseType.LANDING,
            PhaseType.TAKEOFF,
            PhaseType.SITTING_FLAMING,
            PhaseType.SITTING_SCANNING,
            PhaseType.SITTING_ATTACKING,
            PhaseType.DYING
    );
    @Shadow
    @Final
    private Map<RegistryKey<World>, ServerWorld> worlds;
    @Shadow
    @Final
    private ServerMetadata metadata;

    @Shadow
    public abstract SaveProperties getSaveProperties();

    @Shadow
    public abstract PlayerManager getPlayerManager();

    @Inject(method = "initScoreboard", at = @At("TAIL"))
    private void initMixin(PersistentStateManager persistentStateManager, CallbackInfo info) {
        if (!SpeedrunRandomHelper.hasBeenRead()) {
            SessionWorld.setSessionWorld(MSLMod.ooml());
        }
        SpeedrunRandomHelper.setCounts(SpeedrunRandomHelper.overrideOrDefault(getSaveProperties().getGeneratorOptions().getSeed()), 0, 0, 0, 0, false);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickMixin(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        for (ServerPlayerEntity player : getPlayerManager().getPlayerList()) {
            if (MSLMod.ooml() && (player.abilities.creativeMode || player.hasPermissionLevel(1))) {
                MSLMod.eo().mark(0);
            }
        }
        ServerWorld end = worlds.get(World.END);
        if (end.getAliveEnderDragons().size() > 0) {
            PhaseManager phaseManager = worlds.get(World.END).getAliveEnderDragons().get(0).getPhaseManager();

            if (!perchPhases.contains(phaseManager.getCurrent().getType())) {
                SpeedrunRandomHelper.dragonCounter++;
            }
            if (SpeedrunRandomHelper.dragonCounter >= 3600) {
                SpeedrunRandomHelper.dragonCounter = 0;
                phaseManager.setPhase(PhaseType.LANDING_APPROACH);
            }
        }
    }

    @Inject(method = "save", at = @At("TAIL"))
    private void saveMixin(boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<Boolean> info) {
        if (MSLMod.ooml()) {
            MSLMod.eo().checkDatapacks((MinecraftServer) (Object) this);
        }
    }

}
