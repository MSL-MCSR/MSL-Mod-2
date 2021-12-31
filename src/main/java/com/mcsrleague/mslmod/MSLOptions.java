package com.mcsrleague.mslmod;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MSLOptions {
    private static final File file = new File("mcsrleague/options.json");
    private final MSLOptionsInfoJson mslOptionsInfo;
    private int[] timerPos;

    public MSLOptions() {
        Gson gson = new Gson();
        mslOptionsInfo = gson.fromJson(readFile(), MSLOptionsInfoJson.class);
        mslOptionsInfo.ensure();
        timerPos = new int[2];
        timerPos[0] = mslOptionsInfo.timerX;
        timerPos[1] = mslOptionsInfo.timerY;
    }

    public int getDifficulty() {
        return mslOptionsInfo.difficulty;
    }

    public void setDifficulty(int difficulty) {
        mslOptionsInfo.difficulty = difficulty;
    }

    public boolean getTimerEnabled() {
        return mslOptionsInfo.timerEnabled;
    }

    public void setTimerEnabled(boolean timer) {
        mslOptionsInfo.timerEnabled = timer;
    }

    public void setTimerPos(int x, int z) {
        mslOptionsInfo.timerX = x;
        mslOptionsInfo.timerY = z;
        timerPos[0] = x;
        timerPos[1] = z;
    }

    public int[] getTimerPos(){
        return timerPos;
    }

    public void save() {
        save(false);
    }

    private void save(boolean deleteFile) {
        if (deleteFile) {
            file.delete();
        }
        try {
            Gson gson = new Gson();
            String out = gson.toJson(mslOptionsInfo);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(out);
            fileWriter.close();
        } catch (IOException e) {
            if (!deleteFile) {
                save(true);
            } else {
                MSLMod.log(Level.ERROR, "Could not save options.");
            }
        }
    }

    private String readFile() {
        try {
            Scanner scanner = new Scanner(file);
            String out = scanner.nextLine();
            scanner.close();
            return out;
        } catch (FileNotFoundException e) {
            return "{difficulty:1}";
        }
    }

}
