package com.mcsrleague.mslmod.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MSLCreditsScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private static final Identifier DONE = new Identifier("mcsrleague:textures/gui/button/done_small.png");

    private final List<Text> texts;
    private final Screen parent;

    public MSLCreditsScreen(Screen parent) {
        super(new LiteralText("MSL Credits"));
        this.parent = parent;
        texts = getTexts();
    }

    private List<Text> getTexts() {
        List<Text> texts = new ArrayList<>();
        texts.add(new LiteralText("Staff").formatted(Formatting.UNDERLINE).formatted(Formatting.BOLD));
        texts.add(new LiteralText("Mod Developer - ").append(new LiteralText("DuncanRuns").formatted(Formatting.GREEN).formatted(Formatting.BOLD)));
        texts.add(new LiteralText("Art & Design - ").append(new LiteralText("Daferade").formatted(Formatting.DARK_AQUA).formatted(Formatting.BOLD)));
        texts.add(new LiteralText("Website & API Developer - ").append(new LiteralText("Melskaaja").formatted(Formatting.BLUE).formatted(Formatting.BOLD)));
        texts.add(new LiteralText("Streamer and Community Organizer - ").append(new LiteralText("Antoine").formatted(Formatting.GOLD).formatted(Formatting.BOLD)));
        texts.add(new LiteralText(""));
        texts.add(new LiteralText("Mod Testers").formatted(Formatting.UNDERLINE).formatted(Formatting.BOLD));
        texts.add(new LiteralText("Ybot, sl1dr, AutomattPL, CroPro, MinecrAvenger,"));
        texts.add(new LiteralText("Falacias, PrinceofSha, and SunOmega"));
        return texts;
    }

    @Override
    protected void init() {
        addButton(new TexturedButtonWidget(width - 84, height - 24, 80, 20, 0, 0, 20, DONE, 80, 40, button -> onClose()));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        super.render(matrices, mouseX, mouseY, delta);
        int y = height / 2 - 112 + 64;
        for (Text text : texts) {
            y += 12;
            drawCenteredText(matrices, textRenderer, text, width / 2, y, 16777215);
        }
    }

    @Override
    public void onClose() {
        assert client != null;
        client.openScreen(parent);
    }
}
