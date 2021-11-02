package com.mcsrleague.mslmod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class DumbUtil {

    private static boolean dumbUnlocked;
    public static boolean confirmDumbUnlock() {
        File file = new File("mcsrleague/canceltoaster");
        if (!file.exists()) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("This file means you have unlocked the cancel toaster secret button in the options menu.");
                fileWriter.close();
                dumbUnlocked = true;
                return true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return false;
    }

    public static boolean isDumbUnlocked() {
        return dumbUnlocked;
    }

    public static void checkDumbFile() {
        dumbUnlocked = new File("mcsrleague/canceltoaster").exists();
    }
}
