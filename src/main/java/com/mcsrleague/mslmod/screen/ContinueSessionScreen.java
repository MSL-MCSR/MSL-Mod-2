package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.session.SeedSession;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class ContinueSessionScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private static final Identifier QUIT = new Identifier("mcsrleague:textures/gui/button/quit.png");
    private static final Identifier CONTINUE = new Identifier("mcsrleague:textures/gui/button/continue.png");
    private boolean pressedQuit;

    public ContinueSessionScreen() {
        super(new LiteralText("Continue?"));
    }

    @Override
    protected void init() {
        addButton(new TexturedButtonWidget(this.width / 2 - 100, this.height / 4 + 72, 200, 20, 0, 0, 20, CONTINUE, 200, 40, button -> SeedSession.playSessionLevel()));
        addButton(new TexturedButtonWidget(this.width / 2 - 100, this.height / 4 + 96, 200, 20, 0, 0, 20, QUIT, 200, 40, button -> {
            if (!pressedQuit) {
                pressedQuit = true;
            } else {
                cancel();
            }
        }));
        if (MSLMod.isRelog()) {
            SeedSession.playSessionLevel();
            MSLMod.eo().mark(4);
        }
        pressedQuit = false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        fill(matrices, 0, 0, width, height, -15724528);
        assert client != null;
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        drawCenteredString(matrices, textRenderer, "Continue Playing?", width / 2, height / 2 - 48, 16777215);
        if (pressedQuit) {
            drawCenteredString(matrices, textRenderer, "Pressing again will forfeit this seed, and you will receive no points.", width / 2, height / 4 + 120, 16777215);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void cancel() {
        SeedSession oeoe = MSLMod.eo();
        oeoe.end();
        oeoe.save();
        assert client != null;
        client.openScreen(null);
    }


}
