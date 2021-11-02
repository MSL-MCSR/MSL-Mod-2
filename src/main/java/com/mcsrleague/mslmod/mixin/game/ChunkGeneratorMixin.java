package com.mcsrleague.mslmod.mixin.game;

import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StrongholdConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @Shadow @Final private List<ChunkPos> field_24749;

    @Shadow @Final private StructuresConfig config;

    @Shadow @Final protected BiomeSource biomeSource;

    @Shadow @Final private long field_24748;

    /**
     * @author DuncanRuns
     * @reason mine lol
     */
    @Overwrite
    private void method_28509(){
        if (this.field_24749.isEmpty()) {
            StrongholdConfig strongholdConfig = this.config.getStronghold();
            if (strongholdConfig != null && strongholdConfig.getCount() != 0) {
                List<Biome> list = Lists.newArrayList();

                for (Biome biome : this.biomeSource.method_28443()) {
                    if (biome.hasStructureFeature(StructureFeature.STRONGHOLD)) {
                        list.add(biome);
                    }
                }

                int i = strongholdConfig.getDistance();
                int j = strongholdConfig.getCount();
                int k = strongholdConfig.getSpread();
                Random random = new Random();
                random.setSeed(this.field_24748);
                random.nextInt();
                double d = random.nextDouble() * 3.141592653589793D * 2.0D;
                int l = 0;
                int m = 0;

                for(int n = 0; n < j; ++n) {
                    random.nextInt();
                    double e = (double)(4 * i + i * m * 6) + (random.nextDouble() - 0.5D) * (double)i * 2.5D;
                    int o = (int)Math.round(Math.cos(d) * e);
                    int p = (int)Math.round(Math.sin(d) * e);
                    BlockPos blockPos = this.biomeSource.locateBiome((o << 4) + 8, 0, (p << 4) + 8, 112, list, random);
                    if (blockPos != null) {
                        o = blockPos.getX() >> 4;
                        p = blockPos.getZ() >> 4;
                    }

                    this.field_24749.add(new ChunkPos(o, p));
                    d += 6.283185307179586D / (double)k;
                    ++l;
                    if (l == k) {
                        ++m;
                        l = 0;
                        k += 2 * k / (m + 1);
                        k = Math.min(k, j - n);
                        random.nextInt();
                        d += random.nextDouble() * 3.141592653589793D * 2.0D;
                    }
                }

            }
        }
    }
}
