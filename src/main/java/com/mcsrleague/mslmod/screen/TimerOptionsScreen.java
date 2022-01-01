package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.MSLOptions;
import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class TimerOptionsScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private final MSLOptions mslOptions;
    private double x;
    private double y;

    private boolean enabled;
    private boolean shadow;
    private long clickTime;
    private int clickX;
    private int clickY;

    public TimerOptionsScreen() {
        super(new TranslatableText("mcsrleague.timer.title"));
        mslOptions = MSLMod.getOptions();
        x = mslOptions.getTimerPos()[0];
        y = mslOptions.getTimerPos()[1];
        enabled = mslOptions.getTimerEnabled();
        shadow = mslOptions.getTimerShadow();
        clickTime = 0;
        clickX = 0;
        clickY = 0;
    }

    @Override
    public void onClose() {
        assert this.client != null;
        this.client.openScreen(new MSLOptionsScreen());
    }

    public void saveAndClose() {
        mslOptions.setTimerEnabled(enabled);
        mslOptions.setTimerPos(x, y);
        mslOptions.setTimerShadow(shadow);
        mslOptions.save();
        onClose();
    }

    private void updateEnabledButton(AbstractButtonWidget button) {
        button.setMessage(new TranslatableText("mcsrleague.timer.enabled").append(": ").append(new TranslatableText(enabled ? "mcsrleague.timer.on" : "mcsrleague.timer.off")));
    }

    private void updateShadowButton(AbstractButtonWidget button) {
        button.setMessage(new TranslatableText("mcsrleague.timer.shadow").append(": ").append(new TranslatableText(shadow ? "mcsrleague.timer.on" : "mcsrleague.timer.off")));
    }

    @Override
    protected void init() {
        updateEnabledButton(addButton(new MSLButtonWidget(width / 2 - 60, height / 2 - 20, 120, 20, LiteralText.EMPTY, button -> {
            enabled = !enabled;
            updateEnabledButton(button);
        })));
        updateShadowButton(addButton(new MSLButtonWidget(width / 2 - 60, height / 2 + 4, 120, 20, LiteralText.EMPTY, button -> {
            shadow = !shadow;
            updateShadowButton(button);
        })));

        addButton(new MSLButtonWidget(width / 2 - 100, height / 2 + 75, 98, 20, new TranslatableText("mcsrleague.timer.save"), button -> saveAndClose()));
        addButton(new MSLButtonWidget(width / 2 + 2, height / 2 + 75, 98, 20, new TranslatableText("mcsrleague.timer.cancel"), button -> onClose()));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            if (enabled) {
                int dx = 0;
                int dy = 0;
                boolean pressedArrow = false;
                switch (keyCode) {
                    case 262:
                        dx = 1;
                        pressedArrow = true;
                        break;
                    case 263:
                        dx = -1;
                        pressedArrow = true;
                        break;
                    case 264:
                        dy = 1;
                        pressedArrow = true;
                        break;
                    case 265:
                        dy = -1;
                        pressedArrow = true;
                        break;
                }
                if (pressedArrow) {
                    x = ((int) (width * x) + dx) / (double) width;
                    y = ((int) (height * y) + dy) / (double) height;
                }

            }
        }
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else {
            if (enabled && button == 0) {
                long lastClickTime = clickTime;
                int lastX = clickX;
                int lastY = clickY;
                clickX = (int) mouseX;
                clickY = (int) mouseY;
                clickTime = System.currentTimeMillis();
                if (Math.abs(clickTime - lastClickTime) < 250 &&
                        Math.abs(lastX - clickX) < 20 &&
                        Math.abs(lastY - clickY) < 20
                ) {
                    x = clickX / (double) width;
                    y = clickY / (double) height;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        drawCenteredText(matrices, textRenderer, ((TranslatableText) title).formatted(Formatting.BOLD), width / 2, height / 2 - 50, 16777215);
        client.getTextureManager().bindTexture(MSL_LOGO);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, new TranslatableText("mcsrleague.timer.move1"), width / 2, height / 2 + 30, 16777215);
        drawCenteredText(matrices, textRenderer, new TranslatableText("mcsrleague.timer.move2"), width / 2, height / 2 + 45, 16777215);

        if (enabled) {
            int tWidth = textRenderer.getWidth("00:00.00");
            if (shadow) {
                textRenderer.drawWithShadow(matrices, "00:00.00", (int) (width * x) - (x >= 0.5 ? tWidth : 0), (int) (height * y), 16777215);
            } else {
                textRenderer.draw(matrices, "00:00.00", (int) (width * x) - (x >= 0.5 ? tWidth : 0), (int) (height * y), 16777215);
            }
        }
    }

}
