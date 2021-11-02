package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.DumbUtil;
import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.widget.UnselectableButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DumbGameScreen extends Screen {
    private final Screen parent;
    private final Random random;
    private ButtonWidget randomButton;
    private long unlockTime;
    private boolean justUnlocked;
    private String scoreString;
    private String clicksString;
    private List<Long> presses;
    private int clicks;

    public DumbGameScreen(Screen parent) {
        super(new LiteralText("Dumb Game"));
        this.parent = parent;
        random = new Random();
    }

    protected void init() {
        if (DumbUtil.confirmDumbUnlock()) {
            unlockTime = System.currentTimeMillis();
            justUnlocked = true;
        }
        try {
            randomise();
        } catch (Exception ignored) {
        }
        scoreString = "Score: 0.00 cps";
        clicksString = "Clicks: 0";
        clicks = 0;
        presses = new ArrayList<>();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        drawStringWithShadow(matrices, textRenderer, clicksString, 10, 10, 16777215);
        drawStringWithShadow(matrices, textRenderer, scoreString, 10, 10 + 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);

        if (justUnlocked) {
            if (System.currentTimeMillis() - unlockTime < 10000) {
                drawCenteredString(matrices, textRenderer, "Cancel Toaster button is now unlocked in the options menu.", width / 2, height / 2, 16777215);
            } else {
                justUnlocked = false;
            }
        }
    }

    private void calculate() {
        long current = System.currentTimeMillis();
        presses.removeIf(press -> current - press > 10000);
        if (presses.size() > 11) {
            presses.remove(0);
        }
        long first = presses.get(0);
        long last = presses.get(presses.size() - 1);
        float cps = last == first ? 0.0f : presses.size() / (((last - first)) / 1000.0f);
        scoreString = String.format(Locale.US, "Score: %.2f cps", cps);
    }

    private void randomise() {
        if (randomButton != null) {
            randomButton.setAlpha(0.0f);
            randomButton.active = false;
            buttons.remove(randomButton);
            children.remove(randomButton);
        }
        randomButton = addButton(new UnselectableButtonWidget(random.nextInt(width - 20), random.nextInt(height - 20), 20, 20, LiteralText.EMPTY, (button -> {
            randomise();
            presses.add(System.currentTimeMillis());
            calculate();
            justUnlocked = false;
            clicks++;
            clicksString = "Clicks: " + clicks;
        })));
    }

    public void onClose() {
        this.client.openScreen(this.parent);
    }


}
