package com.mcsrleague.mslmod;

import com.mcsrleague.mslmod.session.SeedSession;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
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
    public static String VERSION = "";

    public static Logger LOGGER = LogManager.getLogger();
    private static SeedSession eoeo = SeedSession.fromFile();
    private static boolean relog;
    private static MSLOptions mslOptions;
    private static String token = null;
    private static String completionTime = null;
    private static Timer timer;

    public static boolean isRelog() {
        if (relog) {
            relog = false;
            return true;
        }
        return false;
    }

    public static Timer getTimer() {
        return timer;
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

    public static void complete(String token, String time, boolean givesToken) {
        completionTime = time;
        if (!WarningModsUtil.hasBypass() && givesToken) {
            MSLMod.token = token;
            log(Level.INFO, "Token: " + token);
            try {
                FileWriter fileWriter = new FileWriter("mcsrleague/lasttoken.txt");
                fileWriter.write(token);
                fileWriter.close();
            } catch (IOException e) {
                MSLMod.log(Level.ERROR, "Could not save token to lasttoken.txt.");
                e.printStackTrace();
            }
        }
    }

    public static boolean isCompleted() {
        return eoeo != null && completionTime != null;
    }

    public static String[] takeTokenAndTime() {
        String token = MSLMod.token;
        MSLMod.token = null;
        String completionTime = MSLMod.completionTime;
        MSLMod.completionTime = null;
        return new String[]{token, completionTime};
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    public static MSLOptions getOptions() {
        return mslOptions;
    }


    @Override
    public void onInitialize() {
        VERSION = FabricLoader.getInstance().getModContainer("mcsrleague").get().getMetadata().getVersion().getFriendlyString();

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

        timer = new Timer();

        timer.hide();
        timer.update();
    }

}