package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class OpenWebScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private final Screen parent;

    public OpenWebScreen(Screen parent) {
        super(new TranslatableText("mcsrleague.web.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        parent.init(client, width, height);
        addButton(new MSLButtonWidget(width / 2 - 62, height / 2, 60, 20, new TranslatableText("mcsrleague.web.home"), button -> {
            Util.getOperatingSystem().open("https://mcsrleague.com/");
        }));
        addButton(new MSLButtonWidget(width / 2 + 2, height / 2, 60, 20, new TranslatableText("mcsrleague.web.faq"), button -> {
            Util.getOperatingSystem().open("https://mcsrleague.com/faq/");
        }));
        addButton(new MSLButtonWidget(width / 2 - 40, height / 2 + 24, 80, 20, new TranslatableText("mcsrleague.web.leaderboard"), button -> {
            Util.getOperatingSystem().open("https://mcsrleague.com/leaderboard/week/");
        }));
    }

    @Override
    public void tick() {
        parent.tick();
        super.tick();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        parent.render(matrices, 0, 0, delta);
        fillGradient(matrices, 0, 0, width, height, -1072689136, -804253680);
        drawCenteredText(matrices, textRenderer, title, width / 2, height / 2 - 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
        assert client != null;
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, 16 * (width / 2 - 32), 16 * (height / 2 - 47), 0.0F, 0.0F, 1024, 512, 1024, 512);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        onClose();
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
