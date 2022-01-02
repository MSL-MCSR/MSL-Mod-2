package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.random.SpeedrunRandomUtil;
import com.mcsrleague.mslmod.session.SeedSession;
import com.mcsrleague.mslmod.widget.SquishButtonWidget;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.Objects;

public class ExitOptionsScreen extends Screen {
    private final Screen parent;
    private boolean pressedForfeit;
    private Text confirmText;

    private AbstractButtonWidget forfeitButton;

    public ExitOptionsScreen(Screen parent) {
        super(new TranslatableText("mcsrleague.exit.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addButton(new SquishButtonWidget(width / 2 - 102, height / 4 + 104, 204, 20, new TranslatableText("mcsrleague.exit.reload"), button -> {
            MSLMod.doRelog();
            exitWorld(button);
            MSLMod.eo().unpause();
            MSLMod.eo().mark(4);
        }));
        addButton(new SquishButtonWidget(width / 2 - 102, height / 4 + 72 - 16, 98, 20, new TranslatableText("mcsrleague.exit.restart"), button -> {
            assert client != null;
            MSLMod.eo().unpause();
            String seed = String.valueOf(Objects.requireNonNull(Objects.requireNonNull(client.getServer()).getWorld(World.OVERWORLD)).getSeed());
            long start = MSLMod.eo().getStartTime();
            exitWorld(button);
            SpeedrunRandomUtil.setOverride(SpeedrunRandomUtil.getCurrentSeed());
            SeedSession.createLevel(seed, null, start);
        }));
        forfeitButton = addButton(new SquishButtonWidget(width / 2 + 4, height / 4 + 56, 98, 20, new TranslatableText("mcsrleague.exit.forfeit").formatted(Formatting.RED), button -> {
            if (!pressedForfeit) {
                forfeitButton.y = height / 4 + 16;
                pressedForfeit = true;
            } else {
                SeedSession oeoe = MSLMod.eo();
                oeoe.unpause();
                oeoe.end();
                oeoe.save();
                exitWorld(button);
            }
        }));
        if (pressedForfeit) {
            forfeitButton.y = height / 4 + 16;
        }
        confirmText = new TranslatableText("mcsrleague.exit.confirm");
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, title, width / 2, 40, 16777215);
        if (pressedForfeit) {
            drawCenteredText(matrices, textRenderer, confirmText, forfeitButton.x + forfeitButton.getWidth() / 2, forfeitButton.y + 22, 16733525);
        }
    }

    @Override
    public void onClose() {
        assert client != null;
        client.openScreen(parent);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (!super.mouseClicked(mouseX, mouseY, button)) {
            pressedForfeit = false;
            forfeitButton.y = height / 4 + 56;
            return false;
        } else {
            return true;
        }
    }

    private void exitWorld(ButtonWidget buttonWidget) {
        assert this.client != null;
        boolean bl = this.client.isInSingleplayer();
        boolean bl2 = this.client.isConnectedToRealms();
        buttonWidget.active = false;
        assert this.client.world != null;
        this.client.world.disconnect();
        if (bl) {
            this.client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        } else {
            this.client.disconnect();
        }

        if (bl) {
            this.client.openScreen(new TitleScreen());
        } else if (bl2) {
            RealmsBridge realmsBridge = new RealmsBridge();
            realmsBridge.switchToRealms(new TitleScreen());
        } else {
            this.client.openScreen(new MultiplayerScreen(new TitleScreen()));
        }
    }
}
