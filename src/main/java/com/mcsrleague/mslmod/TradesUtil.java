package com.mcsrleague.mslmod;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.*;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public abstract class TradesUtil {
    public static void changeTrades() {
        Map<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> map = TradeOffers.PROFESSION_TO_LEVELED_TRADE;
        map.put(VillagerProfession.FARMER, copyToFastUtilMap(ImmutableMap.of(1,
                new TradeOffers.Factory[]{
                        new BuyForOneEmeraldFactory(Items.WHEAT, 20, 16, 2),
                        new SellItemFactory(Items.BREAD, 1, 6, 16, 1)}, 2,
                new TradeOffers.Factory[]{
                        new BuyForOneEmeraldFactory(Blocks.PUMPKIN, 6, 12, 10),
                        new SellItemFactory(Items.PUMPKIN_PIE, 1, 4, 5),
                        new SellItemFactory(Items.APPLE, 1, 4, 16, 5)}, 3,
                new TradeOffers.Factory[]{
                        new SellItemFactory(Items.COOKIE, 3, 18, 10),
                        new BuyForOneEmeraldFactory(Blocks.MELON, 4, 12, 20)}, 4,
                new TradeOffers.Factory[]{
                        new SellItemFactory(Blocks.CAKE, 1, 1, 12, 15),
                        new SellSuspiciousStewFactory(StatusEffects.NIGHT_VISION, 100, 15),
                        new SellSuspiciousStewFactory(StatusEffects.JUMP_BOOST, 160, 15),
                        new SellSuspiciousStewFactory(StatusEffects.WEAKNESS, 140, 15),
                        new SellSuspiciousStewFactory(StatusEffects.BLINDNESS, 120, 15),
                        new SellSuspiciousStewFactory(StatusEffects.POISON, 280, 15),
                        new SellSuspiciousStewFactory(StatusEffects.SATURATION, 7, 15)}, 5,
                new TradeOffers.Factory[]{new SellItemFactory(Items.GOLDEN_CARROT, 3, 3, 30),
                        new SellItemFactory(Items.GLISTERING_MELON_SLICE, 4, 3, 30)
                }
        )));
        map.put(VillagerProfession.FISHERMAN, copyToFastUtilMap(ImmutableMap.of(
                1, new TradeOffers.Factory[]{
                        new ProcessItemFactory(Items.COD, 6, Items.COOKED_COD, 6, 16, 1),
                        new SellItemFactory(Items.COD_BUCKET, 3, 1, 16, 1)}, 2,
                new TradeOffers.Factory[]{
                        new BuyForOneEmeraldFactory(Items.COD, 15, 16, 10),
                        new ProcessItemFactory(Items.SALMON, 6, Items.COOKED_SALMON, 6, 16, 5),
                        new SellItemFactory(Items.CAMPFIRE, 2, 1, 5)}, 3,
                new TradeOffers.Factory[]{
                        new BuyForOneEmeraldFactory(Items.SALMON, 13, 16, 20),
                        new SellEnchantedToolFactory(Items.FISHING_ROD, 3, 3, 10, 0.2F)}, 4,
                new TradeOffers.Factory[]{
                        new BuyForOneEmeraldFactory(Items.TROPICAL_FISH, 6, 12, 30)}, 5,
                new TradeOffers.Factory[]{new BuyForOneEmeraldFactory(Items.PUFFERFISH, 4, 12, 30),
                        new TypeAwareBuyForOneEmeraldFactory(1, 12, 30, ImmutableMap.builder().put(VillagerType.PLAINS, Items.OAK_BOAT).put(VillagerType.TAIGA, Items.SPRUCE_BOAT).put(VillagerType.SNOW, Items.SPRUCE_BOAT).put(VillagerType.DESERT, Items.JUNGLE_BOAT).put(VillagerType.JUNGLE, Items.JUNGLE_BOAT).put(VillagerType.SAVANNA, Items.ACACIA_BOAT).put(VillagerType.SWAMP, Items.DARK_OAK_BOAT).build())})));


    }

    private static Int2ObjectOpenHashMap<TradeOffers.Factory[]> copyToFastUtilMap(ImmutableMap<Integer, TradeOffers.Factory[]> map) {
        return new Int2ObjectOpenHashMap<>(map);
    }

    static class SellEnchantedToolFactory implements TradeOffers.Factory {
        private final ItemStack tool;
        private final int basePrice;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellEnchantedToolFactory(Item item, int basePrice, int maxUses, int experience, float multiplier) {
            this.tool = new ItemStack(item);
            this.basePrice = basePrice;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            int i = 5 + random.nextInt(15);
            ItemStack itemStack = EnchantmentHelper.enchant(random, new ItemStack(this.tool.getItem()), i, false);
            int j = Math.min(this.basePrice + i, 64);
            ItemStack itemStack2 = new ItemStack(Items.EMERALD, j);
            return new TradeOffer(itemStack2, itemStack, this.maxUses, this.experience, this.multiplier);
        }
    }

    static class ProcessItemFactory implements TradeOffers.Factory {
        private final ItemStack secondBuy;
        private final int secondCount;
        private final int price;
        private final ItemStack sell;
        private final int sellCount;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public ProcessItemFactory(ItemConvertible item, int secondCount, Item sellItem, int sellCount, int maxUses, int experience) {
            this(item, secondCount, 1, sellItem, sellCount, maxUses, experience);
        }

        public ProcessItemFactory(ItemConvertible item, int secondCount, int price, Item sellItem, int sellCount, int maxUses, int experience) {
            this.secondBuy = new ItemStack(item);
            this.secondCount = secondCount;
            this.price = price;
            this.sell = new ItemStack(sellItem);
            this.sellCount = sellCount;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        @Nullable
        public TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(this.secondBuy.getItem(), this.secondCount), new ItemStack(this.sell.getItem(), this.sellCount), this.maxUses, this.experience, this.multiplier);
        }
    }

    static class TypeAwareBuyForOneEmeraldFactory implements TradeOffers.Factory {
        private final ImmutableMap<Object, Object> map;
        private final int count;
        private final int maxUses;
        private final int experience;

        public TypeAwareBuyForOneEmeraldFactory(int count, int maxUses, int experience, ImmutableMap<Object, Object> map) {
            Registry.VILLAGER_TYPE.stream().filter((villagerType) -> !map.containsKey(villagerType)).findAny().ifPresent((villagerType) -> {
                throw new IllegalStateException("Missing trade for villager type: " + Registry.VILLAGER_TYPE.getId(villagerType));
            });
            this.map = map;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
        }

        @Nullable
        public TradeOffer create(Entity entity, Random random) {
            if (entity instanceof VillagerDataContainer) {
                ItemStack itemStack = new ItemStack((ItemConvertible) this.map.get(((VillagerDataContainer) entity).getVillagerData().getType()), this.count);
                return new TradeOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.experience, 0.05F);
            } else {
                return null;
            }
        }
    }


    static class SellSuspiciousStewFactory implements TradeOffers.Factory {
        final StatusEffect effect;
        final int duration;
        final int experience;
        private final float multiplier;

        public SellSuspiciousStewFactory(StatusEffect effect, int duration, int experience) {
            this.effect = effect;
            this.duration = duration;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        @Nullable
        public TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffectToStew(itemStack, this.effect, this.duration);
            return new TradeOffer(new ItemStack(Items.EMERALD, 1), itemStack, 12, this.experience, this.multiplier);
        }
    }

    static class SellItemFactory implements TradeOffers.Factory {
        private final ItemStack sell;
        private final int price;
        private final int count;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public SellItemFactory(Block block, int price, int count, int maxUses, int experience) {
            this(new ItemStack(block), price, count, maxUses, experience);
        }

        public SellItemFactory(Item item, int price, int count, int experience) {
            this(new ItemStack(item), price, count, 12, experience);
        }

        public SellItemFactory(Item item, int price, int count, int maxUses, int experience) {
            this(new ItemStack(item), price, count, maxUses, experience);
        }

        public SellItemFactory(ItemStack stack, int price, int count, int maxUses, int experience) {
            this(stack, price, count, maxUses, experience, 0.05F);
        }

        public SellItemFactory(ItemStack stack, int price, int count, int maxUses, int experience, float multiplier) {
            this.sell = stack;
            this.price = price;
            this.count = count;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        public TradeOffer create(Entity entity, Random random) {
            return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(this.sell.getItem(), this.count), this.maxUses, this.experience, this.multiplier);
        }
    }


    static class BuyForOneEmeraldFactory implements TradeOffers.Factory {
        private final Item buy;
        private final int price;
        private final int maxUses;
        private final int experience;
        private final float multiplier;

        public BuyForOneEmeraldFactory(ItemConvertible item, int price, int maxUses, int experience) {
            this.buy = item.asItem();
            this.price = price;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = 0.05F;
        }

        public TradeOffer create(Entity entity, Random random) {
            ItemStack itemStack = new ItemStack(this.buy, this.price);
            return new TradeOffer(itemStack, new ItemStack(Items.EMERALD), this.maxUses, this.experience, this.multiplier);
        }
    }

}
