package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.random.SpeedrunRandomUtil;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {
    /**
     * @author DuncanRuns
     * @reason Yoink RNG
     */
    @Overwrite
    private static List<ItemStack> getBarteredItem(@NotNull PiglinEntity piglin) {
        LootTable lootTable = piglin.world.getServer().getLootManager().getTable(LootTables.PIGLIN_BARTERING_GAMEPLAY);
        List<ItemStack> list = lootTable.generateLoot((new LootContext.Builder((ServerWorld) piglin.world)).parameter(LootContextParameters.THIS_ENTITY, piglin).random(SpeedrunRandomUtil.piglinRandom).build(LootContextTypes.BARTER));
        return list;
    }
}
