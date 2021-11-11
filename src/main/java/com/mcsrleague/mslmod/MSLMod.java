package com.mcsrleague.mslmod;

import com.mcsrleague.mslmod.session.SeedSession;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class MSLMod implements ModInitializer {

    public static final String MOD_ID = "mcsrleague";
    public static final String MOD_NAME = "MSL Mod";
    public static Logger LOGGER = LogManager.getLogger();
    private static SeedSession eoeo = SeedSession.fromFile();
    private static boolean relog;
    private static MSLOptions mslOptions;
    private static String token = null;

    public static boolean isRelog() {
        if (relog) {
            relog = false;
            return true;
        }
        return false;
    }

    public static void doRelog() {
        relog = true;
    }

    public static SeedSession eo() {
        return eoeo;
    }

    public static void oem(SeedSession eoeo) {
        MSLMod.eoeo = eoeo;
    }

    public static boolean ooml() {
        return eoeo != null && eoeo.isPlaying();
    }

    public static void complete(String token) {
        MSLMod.token = token;
        try {
            FileWriter fileWriter = new FileWriter("mcsrleague/lasttoken.txt");
            fileWriter.write(token);
            fileWriter.close();
        } catch (IOException e) {
            MSLMod.log(Level.ERROR, "Could not save token to lasttoken.txt.");
            e.printStackTrace();
        }
    }

    public static boolean isCompleted() {
        return eoeo != null && token != null;
    }

    public static String takeToken() {
        String token = MSLMod.token;
        MSLMod.token = null;
        return token;
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    public static MSLOptions getOptions() {
        return mslOptions;
    }


    @Override
    public void onInitialize() {
        WarningModsUtil.checkWarningMods();
        mslOptions = new MSLOptions();
        log(Level.INFO, "Initializing");
        try {
            Files.createDirectories(new File("mcsrleague").toPath());
        } catch (IOException e) {
            log(Level.ERROR, "Error ensuring mcsrleague folder.");
            e.printStackTrace();
        }
        DumbUtil.checkDumbFile();
        TradesUtil.changeTrades();
    }

}