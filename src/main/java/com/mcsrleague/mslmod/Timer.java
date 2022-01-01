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
    private static boolean shadow = true;
    private static double x;
    private static double y;
    private static MinecraftClient client;

    public static void update() {
        client = MinecraftClient.getInstance();
        MSLOptions options = MSLMod.getOptions();
        if (MSLMod.ooml()) {
            startTime = MSLMod.eo().getStartTime();
        } else {
            startTime = System.currentTimeMillis();
        }
        enabled = options.getTimerEnabled();
        shadow = options.getTimerShadow();
        double[] pos = options.getTimerPos();
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
        return (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + decimalFormat.format(seconds);
    }

    public static void render(MatrixStack matrices, TextRenderer textRenderer, int width, int height) {
        if (enabled && shown && MSLMod.ooml() && !client.options.debugEnabled && textRenderer != null) {
            matrices.translate(0, 0, 999.9);
            String tString = getTimeString();
            int tWidth = textRenderer.getWidth(tString);
            if (shadow) {
                textRenderer.drawWithShadow(matrices, tString, (int) (width * x) - (x >= 0.5 ? tWidth : 0), (int) (height * y), 16777215);
            } else {
                textRenderer.draw(matrices, tString, (int) (width * x) - (x >= 0.5 ? tWidth : 0), (int) (height * y), 16777215);
            }
            matrices.pop();
        }
    }
}
