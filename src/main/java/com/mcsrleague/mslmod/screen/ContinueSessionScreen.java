package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.Timer;
import com.mcsrleague.mslmod.session.SeedSession;
import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ContinueSessionScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private boolean pressedQuit;
    private Text warning;

    public ContinueSessionScreen() {
        super(new TranslatableText("mcsrleague.continue.title"));
    }

    @Override
    protected void init() {
        Timer.update();
        Timer.show();
        addButton(new MSLButtonWidget(this.width / 2 - 100, this.height / 4 + 72, 200, 20, new TranslatableText("mcsrleague.continue.continue"), button -> SeedSession.playSessionLevel()));
        addButton(new MSLButtonWidget(this.width / 2 - 100, this.height / 4 + 96, 200, 20, new TranslatableText("mcsrleague.continue.quit"), button -> {
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
        warning = new TranslatableText("mcsrleague.continue.warning");
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        fill(matrices, 0, 0, width, height, -15724528);
        assert client != null;
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        drawCenteredText(matrices, textRenderer, title, width / 2, height / 2 - 48, 16777215);
        if (pressedQuit) {
            drawCenteredText(matrices, textRenderer, warning, width / 2, height / 4 + 120, 16777215);
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
