package com.mcsrleague.mslmod.mixin.game;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.structure.PillagerOutpostGenerator;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.ListPoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PillagerOutpostGenerator.class)
public abstract class PillagerOutpostGeneratorMixin {

    static {
        StructurePoolBasedGenerator.REGISTRY.add(
                new StructurePool(
                        new Identifier("pillager_outpost/towers"),
                        new Identifier("empty"),
                        ImmutableList.of(
                                Pair.of(
                                        new ListPoolElement(
                                                ImmutableList.of(
                                                        new LegacySinglePoolElement("pillager_outpost/watchtower", ImmutableList.of(
                                                                new RuleStructureProcessor(
                                                                        ImmutableList.of(
                                                                                new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.DARK_OAK_PLANKS, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
                                                                                new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.5F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState()),
                                                                                new StructureProcessorRule(new BlockMatchRuleTest(Blocks.TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()),
                                                                                new StructureProcessorRule(new BlockMatchRuleTest(Blocks.WALL_TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()),
                                                                                new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.BIRCH_PLANKS, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState())
                                                                        )
                                                                )
                                                        )),
                                                        new LegacySinglePoolElement("pillager_outpost/watchtower_overgrown", ImmutableList.of(
                                                                new BlockRotStructureProcessor(0.05F),
                                                                new RuleStructureProcessor(
                                                                        ImmutableList.of(
                                                                                new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.DARK_OAK_PLANKS, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
                                                                                new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.5F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState()),
                                                                                new StructureProcessorRule(new BlockMatchRuleTest(Blocks.TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()),
                                                                                new StructureProcessorRule(new BlockMatchRuleTest(Blocks.WALL_TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()),
                                                                                new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.BIRCH_PLANKS, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState())
                                                                        )
                                                                )
                                                        ))

                                                )
                                        ), 1
                                )
                        ),
                        StructurePool.Projection.RIGID
                )
        );

    }
}
