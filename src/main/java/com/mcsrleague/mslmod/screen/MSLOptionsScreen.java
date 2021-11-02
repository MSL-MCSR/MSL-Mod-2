package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.MSLOptions;
import com.mcsrleague.mslmod.infograbber.examples.TestSeedInfoGrabber;
import com.mcsrleague.mslmod.infograbber.examples.FastTestSeedInfoGrabber;
import com.mcsrleague.mslmod.widget.PickableTexturedButtonWidget;
import com.mcsrleague.mslmod.widget.PickableWidget;
import com.mcsrleague.mslmod.widget.PickableWidgetSet;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class MSLOptionsScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private static final Identifier EASY = new Identifier("mcsrleague:textures/gui/button/easy.png");
    private static final Identifier NORMAL = new Identifier("mcsrleague:textures/gui/button/normal.png");
    private static final Identifier HARD = new Identifier("mcsrleague:textures/gui/button/hard.png");
    private static final Identifier DONE = new Identifier("mcsrleague:textures/gui/button/done.png");
    private static final Identifier TEST_RACE = new Identifier("mcsrleague:textures/gui/button/test_race.png");
    private static final Identifier CUSTOM_SEED = new Identifier("mcsrleague:textures/gui/button/custom_seed.png");
    private static final Identifier VIEW_CREDITS = new Identifier("mcsrleague:textures/gui/button/view_credits.png");
    private final MSLOptions mslOptions;
    private PickableWidgetSet difficultyWidgetSet;

    public MSLOptionsScreen() {
        super(new LiteralText("MSL Options"));
        mslOptions = MSLMod.getOptions();
    }

    @Override
    protected void init() {
        assert client != null;
        difficultyWidgetSet = new PickableWidgetSet();

        difficultyWidgetSet.add(addButton(new PickableTexturedButtonWidget(width / 2 - 124, height / 2 - 10, 80, 20, 0, 0, 20, EASY, 80, 40, button -> {
            difficultyWidgetSet.pick((PickableWidget) button);
        })));
        difficultyWidgetSet.add(addButton(new PickableTexturedButtonWidget(width / 2 - 40, height / 2 - 10, 80, 20, 0, 0, 20, NORMAL, 80, 40, button -> {
            difficultyWidgetSet.pick((PickableWidget) button);
        })));
        difficultyWidgetSet.add(addButton(new PickableTexturedButtonWidget(width / 2 + 44, height / 2 - 10, 80, 20, 0, 0, 20, HARD, 80, 40, button -> {
            difficultyWidgetSet.pick((PickableWidget) button);
        })));

        difficultyWidgetSet.pick(mslOptions.getDifficulty() - 1);

        addButton(new TexturedButtonWidget(width / 2 - 154, height / 2 + 40, 100, 20, 0, 0, 20, TEST_RACE, 100, 40, button -> {
            if (hasShiftDown()) {
                client.openScreen(new PlayMSLScreen(new FastTestSeedInfoGrabber()));
            } else {
                client.openScreen(new PlayMSLScreen(new TestSeedInfoGrabber()));
            }
        }));
        addButton(new TexturedButtonWidget(width / 2 -50, height / 2 + 40, 100, 20, 0, 0, 20, CUSTOM_SEED, 100, 40, button -> {
            client.openScreen(new CustomSeedScreen(this));
        }));
        addButton(new TexturedButtonWidget(width / 2 +54, height / 2 + 40, 100, 20, 0, 0, 20, VIEW_CREDITS, 100, 40, button -> {
            client.openScreen(new MSLCreditsScreen(this));
        }));

        addButton(new TexturedButtonWidget(width / 2 - 100, height / 2 + 90, 200, 20, 0, 0, 20, DONE, 200, 40, button -> {
            onClose();
        }));


    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        drawCenteredText(matrices, textRenderer, new LiteralText("MSL Race Options").formatted(Formatting.BOLD), width / 2, height / 2 - 50, 16777215);
        drawCenteredText(matrices, textRenderer, new LiteralText("Starting Difficulty").formatted(Formatting.UNDERLINE), width / 2, height / 2 - 25, 16777215);
        drawCenteredText(matrices, textRenderer, new LiteralText("Other").formatted(Formatting.UNDERLINE), width / 2, height / 2 + 25, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
        mslOptions.setDifficulty(difficultyWidgetSet.getPicked() + 1);
        mslOptions.save();
    }
}
