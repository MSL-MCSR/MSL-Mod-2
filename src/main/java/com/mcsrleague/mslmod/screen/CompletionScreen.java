package com.mcsrleague.mslmod.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class CompletionScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private static final Identifier COPY_TOKEN = new Identifier("mcsrleague:textures/gui/button/copy_token.png");
    private static final Identifier DONE_SMALL = new Identifier("mcsrleague:textures/gui/button/done_small.png");

    private final Screen parent;
    private final String token;
    private Text copyTokenText;

    private Text warningText;
    private int warningTextWidth;
    private long earlyDoneClickTime;
    private boolean copied;

    public CompletionScreen(String token, Screen parent) {
        super(new LiteralText("MSL Race Completed"));
        this.parent = parent;
        this.token = token;
        this.copied = false;
        this.earlyDoneClickTime = 0L;
    }

    @Override
    protected void init() {
        assert client != null;
        this.copyTokenText = new LiteralText("Seed completed! Please submit your token!").formatted(Formatting.BOLD);
        this.warningText = new LiteralText("You must copy your token first!");
        this.warningTextWidth = textRenderer.getWidth(this.warningText);

        addButton(new TexturedButtonWidget(width / 2 - 50, height / 2, 100, 20, 0, 0, 20, COPY_TOKEN, 100, 40, button -> {
            client.keyboard.setClipboard(token);
            this.copied = true;
        }));

        addButton(new TexturedButtonWidget(width - 84, height - 24, 80, 20, 0, 0, 20, DONE_SMALL, 80, 40, button -> {
            if (this.copied) {
                onClose();
            } else {
                earlyDoneClickTime = System.currentTimeMillis();
            }
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        super.render(matrices, mouseX, mouseY, delta);
        if (!copied && System.currentTimeMillis() - earlyDoneClickTime < 5000) {
            drawTextWithShadow(matrices, textRenderer, warningText, width - 88 - warningTextWidth, height - 17, 16733525);
        }
        drawCenteredText(matrices, textRenderer, copyTokenText, width / 2, height / 2 - 20, 16777215);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void onClose() {
        assert client != null;
        client.openScreen(parent);
    }
}
