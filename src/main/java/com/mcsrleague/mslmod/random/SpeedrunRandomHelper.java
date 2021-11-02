package com.mcsrleague.mslmod.random;

public class SpeedrunRandomHelper {

    public static final CountedRandom eyeRandom = new CountedRandom();
    public static final CountedRandom blazeRandom = new CountedRandom();
    public static final CountedRandom piglinRandom = new CountedRandom();
    public static int dragonCounter = 0;

    private static boolean beenRead = false;

    private static boolean override = false;
    private static long overrideSeed = 0L;
    private static long currentSeed = 0L;

    public static void setOverride(long seed){
        override = true;
        overrideSeed = seed;
    }

    public static boolean hasOverride() {
        return override;
    }

    public static long overrideOrDefault(long defaultSeed){
        if(override){
            override = false;
            return overrideSeed;
        } else {
            return defaultSeed;
        }
    }

    public static void setCounts(long seed, int barter, int blaze, int eye, int dragonCounter, boolean fromRead) {
        if (fromRead) {
            beenRead = true;
        } else if (beenRead) {
            beenRead = false;
            return;
        }

        SpeedrunRandomHelper.dragonCounter = dragonCounter;

        eyeRandom.setSeed(seed, eye);
        blazeRandom.setSeed(seed, blaze);
        piglinRandom.setSeed(seed, barter);

        currentSeed = seed;
    }

    public static long getCurrentSeed() {
        return currentSeed;
    }

    public static boolean hasBeenRead() {
        return beenRead;
    }
}