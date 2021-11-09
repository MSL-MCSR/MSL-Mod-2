package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class MSLCreditsScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private final List<Text> texts;
    private final Screen parent;

    public MSLCreditsScreen(Screen parent) {
        super(new TranslatableText("mcsrleague.credits.title"));
        this.parent = parent;
        texts = getTexts();
    }

    private List<Text> getTexts() {
        List<Text> texts = new ArrayList<>();
        texts.add(new TranslatableText("mcsrleague.credits.staff").formatted(Formatting.UNDERLINE).formatted(Formatting.BOLD));
        texts.add(new TranslatableText("mcsrleague.credits.moddev").append(new LiteralText(" - ")).append(new LiteralText("DuncanRuns").formatted(Formatting.GREEN).formatted(Formatting.BOLD)));
        texts.add(new TranslatableText("mcsrleague.credits.art").append(new LiteralText(" - ")).append(new LiteralText("Daferade").formatted(Formatting.DARK_AQUA).formatted(Formatting.BOLD)));
        texts.add(new TranslatableText("mcsrleague.credits.webdev").append(new LiteralText(" - ")).append(new LiteralText("Melskaaja").formatted(Formatting.BLUE).formatted(Formatting.BOLD)));
        texts.add(new TranslatableText("mcsrleague.credits.streamer").append(new LiteralText(" - ")).append(new LiteralText("Antoine").formatted(Formatting.GOLD).formatted(Formatting.BOLD)));
        texts.add(LiteralText.EMPTY);
        texts.add(new TranslatableText("mcsrleague.credits.testers").formatted(Formatting.UNDERLINE).formatted(Formatting.BOLD));
        texts.add(new LiteralText("Ybot, sl1dr, AutomattPL, CroPro, MinecrAvenger,"));
        texts.add(new LiteralText("Falacias, PrinceofSha, and SunOmega"));
        return texts;
    }

    @Override
    protected void init() {
        addButton(new MSLButtonWidget(width - 84, height - 24, 80, 20, new TranslatableText("mcsrleague.credits.done"), button -> onClose()));
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
