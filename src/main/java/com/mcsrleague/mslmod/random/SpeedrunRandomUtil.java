package com.mcsrleague.mslmod.random;

import org.apache.commons.lang3.StringUtils;

import java.util.OptionalLong;

public abstract class SpeedrunRandomUtil {

    public static final CountedRandom eyeRandom = new CountedRandom();
    public static final CountedRandom blazeRandom = new CountedRandom();
    public static final CountedRandom piglinRandom = new CountedRandom();
    public static int dragonCounter = 0;

    private static long currentSeed = 0L;


    public static OptionalLong tryParseLong(String string) {
        try {
            return OptionalLong.of(Long.parseLong(string));
        } catch (NumberFormatException var2) {
            return OptionalLong.empty();
        }
    }

    public static long stringToSeed(String string) {

        OptionalLong optionalLong4;
        if (StringUtils.isEmpty(string)) {
            optionalLong4 = OptionalLong.empty();
        } else {
            OptionalLong optionalLong2 = tryParseLong(string);
            if (optionalLong2.isPresent() && optionalLong2.getAsLong() != 0L) {
                optionalLong4 = optionalLong2;
            } else {
                optionalLong4 = OptionalLong.of(string.hashCode());
            }
        }

        if (optionalLong4.isPresent()) {
            return optionalLong4.getAsLong();
        } else {
            return 0L;
        }

    }

    public static void setCounts(long seed) {
        setCounts(seed, 0, 0, 0, 0);
    }

    public static void setCounts(long seed, int barter, int blaze, int eye, int dragonCounter) {
        SpeedrunRandomUtil.dragonCounter = dragonCounter;

        eyeRandom.setSeed(seed, eye);
        blazeRandom.setSeed(seed, blaze);
        piglinRandom.setSeed(seed, barter);

        currentSeed = seed;
    }

    public static long getCurrentSeed() {
        return currentSeed;
    }
}