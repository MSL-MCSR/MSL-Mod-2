package com.mcsrleague.mslmod.mixin.game;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.SurfaceChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(SurfaceChunkGenerator.class)
public abstract class SurfaceChunkGeneratorMixin extends ChunkGenerator {
    public SurfaceChunkGeneratorMixin(BiomeSource biomeSource, StructuresConfig structuresConfig) {
        super(biomeSource, structuresConfig);
    }

    @Inject(method = "getEntitySpawnList", at = @At("HEAD"), cancellable = true)
    private void removeTempleSpawnsMixin(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos, CallbackInfoReturnable<List<Biome.SpawnEntry>> cir) {
        if (accessor.method_28388(pos, false, StructureFeature.JUNGLE_PYRAMID).hasChildren() ||
                accessor.method_28388(pos, false, StructureFeature.DESERT_PYRAMID).hasChildren() ||
                accessor.method_28388(pos.add(0, 12, 0), false, StructureFeature.DESERT_PYRAMID).hasChildren() ||
                accessor.method_28388(pos.add(0, 4, 0), false, StructureFeature.JUNGLE_PYRAMID).hasChildren() ||
                accessor.method_28388(pos, false, StructureFeature.PILLAGER_OUTPOST).hasChildren()) {
            cir.setReturnValue(Collections.emptyList());
        }
    }
}
