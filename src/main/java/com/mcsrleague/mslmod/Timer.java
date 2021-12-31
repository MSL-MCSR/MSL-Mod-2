package com.mcsrleague.mslmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

import java.text.DecimalFormat;

public class Timer {
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private static long startTime;
    private static boolean enabled = false;
    private static boolean shown = false;
    private static int x;
    private static int y;
    private static MinecraftClient client;

    public static void update() {
        client = MinecraftClient.getInstance();
        if (MSLMod.ooml()) {
            startTime = MSLMod.eo().getStartTime();
        } else {
            startTime = System.currentTimeMillis();
        }
        enabled = MSLMod.getOptions().getTimerEnabled();
        int[] pos = MSLMod.getOptions().getTimerPos();
        x = pos[0];
        y = pos[1];
    }

    public static void show() {
        shown = true;
    }

    public static void hide() {
        shown = false;
    }

    public static String getTimeString() {
        float time = (System.currentTimeMillis() - startTime) / 1000f;
        if (time >= 6000) {
            return "99:" + decimalFormat.format(99.999);
        }
        int minutes = (int) time / 60;
        float seconds = time - 60 * minutes;
        return (minutes < 9 ? "0" : "") + minutes + ":" + (seconds < 9 ? "0" : "") + decimalFormat.format(seconds);
    }

    public static void render(MatrixStack matrices, TextRenderer textRenderer) {
        if (enabled && shown && MSLMod.ooml() && !client.options.debugEnabled) {
            textRenderer.draw(matrices, getTimeString(), x, y, 16777215);
        }
    }
}
