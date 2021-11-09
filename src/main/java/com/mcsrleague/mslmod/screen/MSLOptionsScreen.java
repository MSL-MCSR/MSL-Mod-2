package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.MSLOptions;
import com.mcsrleague.mslmod.infograbber.TestSeedInfoGrabber;
import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import com.mcsrleague.mslmod.widget.PickableMSLButtonWidget;
import com.mcsrleague.mslmod.widget.PickableWidget;
import com.mcsrleague.mslmod.widget.PickableWidgetSet;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class MSLOptionsScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private final MSLOptions mslOptions;
    private PickableWidgetSet difficultyWidgetSet;
    private TranslatableText difficultyText;
    private TranslatableText otherText;

    public MSLOptionsScreen() {
        super(new TranslatableText("mcsrleague.options.title"));
        mslOptions = MSLMod.getOptions();
    }

    @Override
    protected void init() {
        assert client != null;
        difficultyWidgetSet = new PickableWidgetSet();

        difficultyWidgetSet.add(addButton(new PickableMSLButtonWidget(width / 2 - 124, height / 2 - 10, 80, 20, new TranslatableText("mcsrleague.options.easy"), button -> {
            difficultyWidgetSet.pick((PickableWidget) button);
        })));
        difficultyWidgetSet.add(addButton(new PickableMSLButtonWidget(width / 2 - 40, height / 2 - 10, 80, 20, new TranslatableText("mcsrleague.options.normal"), button -> {
            difficultyWidgetSet.pick((PickableWidget) button);
        })));
        difficultyWidgetSet.add(addButton(new PickableMSLButtonWidget(width / 2 + 44, height / 2 - 10, 80, 20, new TranslatableText("mcsrleague.options.hard"), button -> {
            difficultyWidgetSet.pick((PickableWidget) button);
        })));

        difficultyWidgetSet.pick(mslOptions.getDifficulty() - 1);

        addButton(new MSLButtonWidget(width / 2 - 154, height / 2 + 40, 100, 20, new TranslatableText("mcsrleague.options.testrace"), button -> {
            if (hasShiftDown()) {
                client.openScreen(new PlayMSLScreen(new TestSeedInfoGrabber(true)));
            } else {
                client.openScreen(new PlayMSLScreen(new TestSeedInfoGrabber(false)));
            }
        }));
        addButton(new MSLButtonWidget(width / 2 - 50, height / 2 + 40, 100, 20, new TranslatableText("mcsrleague.options.customseed"), button -> {
            client.openScreen(new CustomSeedScreen(this));
        }));
        addButton(new MSLButtonWidget(width / 2 + 54, height / 2 + 40, 100, 20, new TranslatableText("mcsrleague.options.credits"), button -> {
            client.openScreen(new MSLCreditsScreen(this));
        }));

        addButton(new MSLButtonWidget(width / 2 - 100, height / 2 + 90, 200, 20, new TranslatableText("mcsrleague.options.done"), button -> {
            onClose();
        }));

        difficultyText = new TranslatableText("mcsrleague.options.difficulty");
        otherText = new TranslatableText("mcsrleague.options.other");

    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        drawCenteredText(matrices, textRenderer, ((TranslatableText) title).formatted(Formatting.BOLD), width / 2, height / 2 - 50, 16777215);
        drawCenteredText(matrices, textRenderer, difficultyText.formatted(Formatting.UNDERLINE), width / 2, height / 2 - 25, 16777215);
        drawCenteredText(matrices, textRenderer, otherText.formatted(Formatting.UNDERLINE), width / 2, height / 2 + 25, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
        mslOptions.setDifficulty(difficultyWidgetSet.getPicked() + 1);
        mslOptions.save();
    }
}
