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

    public MSLOptions() {
        Gson gson = new Gson();
        mslOptionsInfo = gson.fromJson(readFile(), MSLOptionsInfoJson.class);
        if (mslOptionsInfo.difficulty == null) {
            mslOptionsInfo.difficulty = 1;
        }
    }

    public void setDifficulty(int difficulty) {
        mslOptionsInfo.difficulty = difficulty;
    }
    public int getDifficulty(){
        return mslOptionsInfo.difficulty;
    }

    public void save() {
        save(false);
    }

    private void save(boolean deleteFile) {
        if(deleteFile){
            file.delete();
        }
        try {
            Gson gson = new Gson();
            String out = gson.toJson(mslOptionsInfo);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(out);
            fileWriter.close();
        } catch (IOException e) {
            if(!deleteFile){
                save(true);
            }else {
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
