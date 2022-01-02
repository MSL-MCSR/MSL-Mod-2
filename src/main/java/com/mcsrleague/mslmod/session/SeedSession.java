package com.mcsrleague.mslmod.session;

import com.google.gson.Gson;
import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.mixin.access.CreateWorldScreenAccess;
import com.mcsrleague.mslmod.mixin.access.MoreOptionsDialogAccess;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.*;

public class SeedSession {
    private static final File file = new File("mcsrleague/ss");
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
            "motiono",
            "starlight",
            "fabric-resource-loader-v0"
    ));

    private final long creationTime;
    private long startTime;
    private boolean playing;
    private boolean hasCheats;
    private boolean hasBadMods;
    private boolean hasDatapacks;
    private boolean hasRelog;
    private boolean hasLongPause;
    private long pauseStart;
    private boolean givesToken;

    public SeedSession() {
        creationTime = System.currentTimeMillis();
        playing = true;
        givesToken = true;
        init();
    }

    public SeedSession(SeedSessionInfoJson saveInfo) {
        creationTime = saveInfo.creationTime;
        startTime = saveInfo.startTime;
        playing = saveInfo.playing;
        givesToken = saveInfo.givesToken;
        init();
    }

    @Nullable
    public static SeedSession fromFile() {
        try {
            Scanner scanner = new Scanner(file);
            String string = scanner.nextLine();
            scanner.close();
            Gson gson = new Gson();
            SeedSessionInfoJson saveInfo = gson.fromJson(string, SeedSessionInfoJson.class);
            return new SeedSession(saveInfo);
        } catch (Exception e) {
            return null;
        }
    }

    public static void playSessionLevel() {
        try {
            MinecraftClient client = MinecraftClient.getInstance();
            MSLMod.getTimer().update();
            MSLMod.getTimer().show();
            client.method_29970(new SaveLevelScreen(new TranslatableText("selectWorld.data_read")));
            client.startIntegratedServer(MSLMod.eo().getWorldName());
        } catch (Exception e) {
            MSLMod.log(Level.ERROR, "Could not join back.");
            e.printStackTrace();
        }
    }

    public static void createLevel(String worldSeed, Screen parent, boolean givesToken) {
        createLevel(worldSeed, parent, 0L, givesToken);
    }

    public static void createLevel(String worldSeed, Screen parent, Long startTime, boolean givesToken) {
        SeedSession oeoe = new SeedSession();
        oeoe.setGivesToken(givesToken);
        MSLMod.oem(oeoe);

        CreateWorldScreen createWorldScreen = new CreateWorldScreen(parent);
        MinecraftClient client = MinecraftClient.getInstance();
        assert client != null;
        client.openScreen(createWorldScreen);
        CreateWorldScreenAccess cwsAccess = (CreateWorldScreenAccess) createWorldScreen;
        MoreOptionsDialogAccess modAccess = (MoreOptionsDialogAccess) (createWorldScreen.moreOptionsDialog);

        cwsAccess.getLevelNameField().setText(oeoe.getWorldName());
        int difficultyInt = MSLMod.getOptions().getDifficulty();
        cwsAccess.setField_24290(Difficulty.byOrdinal(difficultyInt));
        cwsAccess.setField_24289(Difficulty.byOrdinal(difficultyInt));
        modAccess.getSeedTextField().setText(worldSeed);
        cwsAccess.invokeCreateLevel();
        runSetupCommands();

        if (startTime == 0) {
            startTime = System.currentTimeMillis();
        }
        oeoe.setStartTime(startTime);
        oeoe.save();

        MSLMod.getTimer().update();
        MSLMod.getTimer().show();
    }

    private static void runSetupCommands() {
        String[] commands = new String[]{
                "gamerule spawnRadius 0",
                "gamerule doPatrolSpawning false",
                "gamerule doInsomnia false",
                "weather clear 1000000"
        };
        MinecraftClient client = MinecraftClient.getInstance();
        assert client != null;
        assert client.getServer() != null;
        CommandManager commandManager = client.getServer().getCommandManager();
        ServerCommandSource serverCommandSource = client.getServer().getCommandSource();
        for (String command : commands) {
            commandManager.execute(serverCommandSource, command);
        }
    }

    public static void createLevel(String worldSeed, Screen parent, Long startTime) {
        createLevel(worldSeed, parent, startTime, true);
    }

    private static String getTimeString(long startTime, long endTime) {
        double time = (endTime - startTime)/1000.0d;
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        if (time >= 6000) {
            return "99:" + decimalFormat.format(99.999);
        }
        int minutes = (int) time / 60;
        double seconds = time - 60 * minutes;
        return (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + decimalFormat.format(seconds);
    }

    public void setGivesToken(boolean givesToken) {
        this.givesToken = givesToken;
    }

    public void pause() {
        if (pauseStart == 0L) {
            pauseStart = System.currentTimeMillis();
        }
    }

    public void unpause() {
        if (pauseStart != 0L) {
            long dif = System.currentTimeMillis() - pauseStart;
            if (dif > 30000L) {
                mark(1);
            }
            pauseStart = 0L;
        }
    }

    public void mark(int mark) {
        switch (mark) {
            case 0:
                hasCheats = true;
                break;
            case 1:
                hasLongPause = true;
                break;
            case 2:
                hasDatapacks = true;
                break;
            case 3:
                hasBadMods = true;
                break;
            case 4:
                hasRelog = true;
                break;
        }
    }

    public void checkDatapacks(MinecraftServer server) {
        if (server.getDataPackManager().getProfiles().size() > 1) {
            mark(2);
        }
    }

    public String[] generateTokenAndTime(ServerPlayerEntity player) {
        String token;
        long time = System.currentTimeMillis();
        try {
            checkDatapacks(Objects.requireNonNull(player.getServer()));
            ServerWorld ee = Objects.requireNonNull(player.getServer()).getWorld(World.OVERWORLD);
            String aa = Long.toHexString(time - (time % 51) + getTimeAdd());
            assert ee != null;
            String cc = Long.toHexString(ee.getSeed());
            String bb = player.getUuidAsString().replace("-", "");
            String dd = Integer.toHexString(player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_ONE_MINUTE)));
            token = aa + "g" + bb + "g" + cc + "g" + dd + "g" + Long.toHexString(startTime);
        } catch (Exception exception) {
            exception.printStackTrace();
            token = "bad";
        }
        return new String[]{token, getTimeString(startTime, time)};
    }

    private int getTimeAdd() {
        return (hasCheats ? 1 : 0) + (hasLongPause ? 2 : 0) + (hasDatapacks ? 4 : 0) + (hasBadMods ? 8 : 0) + (hasRelog ? 16 : 0);
    }

    private void init() {
        hasCheats = false;
        hasDatapacks = false;
        hasBadMods = false;
        hasLongPause = false;
        hasRelog = false;
        pauseStart = 0;
        checkMods();
    }

    private void checkMods() {
        for (ModContainer modContainer : FabricLoader.getInstance().getAllMods()) {
            String modId = modContainer.getMetadata().getId();
            if (!validMods.contains(modId)) {
                mark(3);
                break;
            }
        }
    }

    public void save() {
        String json = getJsonString();
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
        } catch (Exception e) {
            MSLMod.log(Level.ERROR, "Could not save run info.");
            e.printStackTrace();
        }
    }

    private String getJsonString() {
        SeedSessionInfoJson saveInfo = new SeedSessionInfoJson();
        saveInfo.creationTime = creationTime;
        saveInfo.playing = playing;
        saveInfo.startTime = startTime;
        saveInfo.givesToken = givesToken;
        Gson gson = new Gson();
        return gson.toJson(saveInfo);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getWorldName() {
        return "MSL Race " + getCreationTime();
    }

    public boolean isPlaying() {
        return playing;
    }

    public void end() {
        MSLMod.getTimer().hide();
        playing = false;
    }

    public void complete(ServerPlayerEntity player) {
        String[] tokenAndTime = generateTokenAndTime(player);
        end();
        save();
        MSLMod.complete(tokenAndTime[0], tokenAndTime[1], givesToken);
    }


}
