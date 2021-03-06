package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.random.SpeedrunRandomUtil;
import com.mcsrleague.mslmod.session.SessionWorldUtil;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
public abstract class LevelPropertiesMixin {
    @Inject(method = "method_29029", at = @At("TAIL"))
    private static void readLevelDat(Dynamic<Tag> dynamic, DataFixer dataFixer, int i, CompoundTag compoundTag, LevelInfo levelInfo, SaveVersionInfo saveVersionInfo, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> info) {
        int blaze = dynamic.get("BlazeCount").asInt(0);
        int barter = dynamic.get("BarterCount").asInt(0);
        int eye = dynamic.get("EyeCount").asInt(0);
        int dragonCounter = dynamic.get("DragonPerch").asInt(0);
        long dropSeed = dynamic.get("DropSeed").asLong(dynamic.get("WorldGenSettings").get("seed").asLong(0L));
        SpeedrunRandomUtil.setCounts(dropSeed, barter, blaze, eye, dragonCounter);
        SessionWorldUtil.setSessionStart(dynamic.get("SessionStart").asLong(0L));
    }

    @Inject(method = "updateProperties", at = @At("TAIL"))
    private void addLevelDat(RegistryTracker registryTracker, CompoundTag compoundTag, CompoundTag compoundTag2, CallbackInfo info) {
        compoundTag.putInt("BlazeCount", SpeedrunRandomUtil.blazeRandom.getCount());
        compoundTag.putInt("BarterCount", SpeedrunRandomUtil.piglinRandom.getCount());
        compoundTag.putInt("EyeCount", SpeedrunRandomUtil.eyeRandom.getCount());
        compoundTag.putInt("DragonPerch", SpeedrunRandomUtil.dragonCounter);
        compoundTag.putLong("SessionStart", SessionWorldUtil.getSessionStart());
        compoundTag.putLong("DropSeed", SpeedrunRandomUtil.getCurrentSeed());
    }
}
