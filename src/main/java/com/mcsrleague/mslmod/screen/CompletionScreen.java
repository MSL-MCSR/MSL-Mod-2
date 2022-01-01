package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class CompletionScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");

    private final Screen parent;
    private final String token;
    private final boolean hasToken;

    private final Text copyTokenText;
    private final Text finishTimeText;
    private final Text warningText;

    private int warningTextWidth;
    private long earlyDoneClickTime;
    private boolean copied;

    public CompletionScreen(String token, String completionTime, Screen parent) {
        super(new TranslatableText("mcsrleague.completion.title"));
        this.parent = parent;
        this.token = token;
        this.copied = false;
        this.earlyDoneClickTime = 0L;
        this.hasToken = token != null;

        this.copyTokenText = new TranslatableText("mcsrleague.completion.tokeninfo").formatted(Formatting.BOLD);
        this.finishTimeText = new TranslatableText("mcsrleague.completion.time").append(" ").append(completionTime).append("!");
        this.warningText = new TranslatableText("mcsrleague.completion.copywarning");
    }

    @Override
    protected void init() {
        assert client != null;
        this.warningTextWidth = textRenderer.getWidth(this.warningText);

        if(hasToken) {
            addButton(new MSLButtonWidget(width / 2 - 50, height / 2 + 15, 100, 20, new TranslatableText("mcsrleague.completion.token"), button -> {
                client.keyboard.setClipboard(token);
                this.copied = true;
            }));
        }

        addButton(new MSLButtonWidget(width - 84, height - 24, 80, 20, new TranslatableText("mcsrleague.completion.done"), button -> {
            if ((!this.hasToken) || this.copied) {
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
        drawCenteredText(matrices, textRenderer, finishTimeText, width / 2, height / 2 - 20, 16777215);

        if (hasToken) {
            drawCenteredText(matrices, textRenderer, copyTokenText, width / 2, height / 2 - 5, 16777215);
            if (!copied && System.currentTimeMillis() - earlyDoneClickTime < 5000) {
                drawTextWithShadow(matrices, textRenderer, warningText, width - 88 - warningTextWidth, height - 17, 16733525);
            }
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return !hasToken;
    }

    @Override
    public void onClose() {
        assert client != null;
        client.openScreen(parent);
    }
}
