package com.mcsrleague.mslmod;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.*;

public abstract class WarningModsUtil {
    private static final List<String> validMods = new ArrayList<>(Arrays.asList(
            "fabricloader",
            "minecraft",
            "java",
            "mcsrleague",
            "sodium",
            "lithium",
            "lithium-api",
            "phosphor",
            "lazydfu",
            "voyager",
            "krypton",
            "starlight",
            "dynamicmenufps",
            "fabric-resource-loader-v0",
            "motiono"
    ));
    private static Map<String, String> warningMods = null;
    private static boolean warningModsEnabled;
    private static List<String> foundWarningMods;
    private static boolean bypass = false;
    private static boolean tm = false;

    public static boolean isTm() {
        return tm;
    }

    public static boolean hasWarningMods() {
        return warningModsEnabled;
    }

    public static boolean hasBypass() {
        return bypass;
    }

    public static List<String> getFoundWarningMods() {
        return new ArrayList<>(foundWarningMods);
    }

    // Custom names for mods can be listed here to replace the default metadata name of the mod
    private static void genWarningMods() {
        warningMods = new HashMap<>();
        warningMods.put("fast_reset", "Fast Reset");
        warningMods.put("infinipearl", "InfiniPearl");
        warningMods.put("autoreset", "Auto Reset");
        warningMods.put("icarus", "Icarus");
        warningMods.put("fabric", "Fabric API");
        warningMods.put("pogloot", "Pog Loot");
        warningMods.put("speedrun_mayhem", "MSM's Mod");
        warningMods.put("chunkmod", "Chunk Mod");
        warningMods.put("lazystronghold", "Lazy Stronghold");
        warningMods.put("noverworld", "Noverworld Mod");
        warningMods.put("moonlight", "Moonlight");
        warningMods.put("c2me", "C^2M-Engine (C2ME)");
    }

    public static void checkWarningMods() {
        if (warningMods == null) {
            genWarningMods();
        }
        foundWarningMods = new ArrayList<>();

        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            String modId = modContainer.getMetadata().getId();
            if (!validMods.contains(modId)) {
                if (modId.equals("msl-seed-finding")) {
                    tm = true;
                }
                warningModsEnabled = true;
                foundWarningMods.add(warningMods.getOrDefault(modId, modContainer.getMetadata().getName()));
            }
        }
    }

    public static void setBypass() {
        bypass = true;
    }
}