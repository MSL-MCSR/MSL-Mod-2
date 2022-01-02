package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.WarningModsUtil;
import com.mcsrleague.mslmod.widget.SquishButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ModsWarningScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");

    private final List<Text> textList;
    private AbstractButtonWidget continueButton;
    private AbstractButtonWidget quitButton;
    private Text incompatibleText;

    public ModsWarningScreen() {
        super(new LiteralText("Warning Screen"));
        textList = new ArrayList<>();
        for (String mod : WarningModsUtil.getFoundWarningMods()) {
            textList.add(new LiteralText(mod));
        }
    }

    @Override
    protected void init() {
        assert this.client != null;
        quitButton = this.addButton(new SquishButtonWidget(this.width - 102, height - 24, 98, 20, new TranslatableText("menu.quit"), (buttonWidget) -> {
            this.client.scheduleStop();
        }));
        continueButton = this.addButton(new SquishButtonWidget(this.width - 102, height - 48, 98, 20, new LiteralText("Continue Anyway"), (buttonWidget) -> {
            if(hasShiftDown()){
                WarningModsUtil.setBypass();
                onClose();
            }
        }));
        incompatibleText = new TranslatableText("mcsrleague.warning.incompatible").append(new LiteralText(":"));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);

        int y = height / 2 - 36;
        drawCenteredText(matrices, textRenderer, incompatibleText, width / 2, y, 16777215);
        for (Text text : textList) {
            y += 15;
            drawCenteredText(matrices, textRenderer, text, width / 2, y, 16777215);
        }

        quitButton.render(matrices, mouseX, mouseY, delta);
        if(hasShiftDown()){
            continueButton.active = true;
            continueButton.render(matrices, mouseX, mouseY, delta);
        } else{
            continueButton.active = false;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
