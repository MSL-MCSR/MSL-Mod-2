package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.MSLOptions;
import com.mcsrleague.mslmod.infograbber.HttpsInfoGrabber;
import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class MSLOptionsScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private final MSLOptions mslOptions;
    private TranslatableText raceOptionsText;
    private TranslatableText extrasText;
    private int difficulty;

    public MSLOptionsScreen() {
        super(new TranslatableText("mcsrleague.options.title"));
        mslOptions = MSLMod.getOptions();
    }

    private void updateDifficultyButton(AbstractButtonWidget button) {
        Text difficultyText = new LiteralText("None");
        switch (difficulty) {
            case 1:
                difficultyText = new TranslatableText("mcsrleague.options.easy");
                break;
            case 2:
                difficultyText = new TranslatableText("mcsrleague.options.normal");
                break;
            case 3:
                difficultyText = new TranslatableText("mcsrleague.options.hard");
                break;
        }
        button.setMessage(new TranslatableText("mcsrleague.options.difficulty").append(": ").append(difficultyText));
    }

    @Override
    protected void init() {
        assert client != null;

        AbstractButtonWidget difficultyButton = addButton(new MSLButtonWidget(width / 2 - 154, height / 2 - 10, 152, 20, new LiteralText(""), button -> {
            difficulty += 1;
            if (difficulty > 3) {
                difficulty = 1;
            }
            updateDifficultyButton(button);
        }));
        difficulty = mslOptions.getDifficulty();
        updateDifficultyButton(difficultyButton);

        addButton(new MSLButtonWidget(width / 2 + 2, height / 2 - 10, 152, 20, new TranslatableText("mcsrleague.options.timeroptions"), button -> {
            onClose();
            this.client.openScreen(new TimerOptionsScreen());
        }));


        addButton(new MSLButtonWidget(width / 2 - 154, height / 2 + 40, 100, 20, new TranslatableText("mcsrleague.options.testrace"), button -> {
            client.openScreen(new PlayMSLScreen(new HttpsInfoGrabber("https://mcsrleague.com/api/testseed"), false));
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

        raceOptionsText = new TranslatableText("mcsrleague.options.raceoptions");
        extrasText = new TranslatableText("mcsrleague.options.extras");

    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        drawCenteredText(matrices, textRenderer, ((TranslatableText) title).formatted(Formatting.BOLD), width / 2, height / 2 - 50, 16777215);
        drawCenteredText(matrices, textRenderer, raceOptionsText.formatted(Formatting.UNDERLINE), width / 2, height / 2 - 25, 16777215);
        drawCenteredText(matrices, textRenderer, extrasText.formatted(Formatting.UNDERLINE), width / 2, height / 2 + 25, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();
        mslOptions.setDifficulty(difficulty);
        mslOptions.save();
    }
}
