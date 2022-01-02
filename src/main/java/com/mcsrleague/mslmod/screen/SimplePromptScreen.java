package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SimplePromptScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private final Text warningText;
    private final Text okText;
    private final boolean exitOnEsc;
    private final Screen parent;

    public SimplePromptScreen(Screen parent, Text warningText, Text okText, boolean exitOnEsc) {
        super(new LiteralText("World Warning Screen"));
        this.parent = parent;
        this.warningText = warningText;
        this.okText = okText;
        this.exitOnEsc = exitOnEsc;
    }

    @Override
    protected void init() {
        addButton(new MSLButtonWidget(width / 2 - 50, height / 2 + 15, 100, 20, okText, button -> {
            onClose();
        }));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return exitOnEsc;
    }

    @Override
    public void onClose() {
        assert this.client != null;
        this.client.openScreen(parent);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, textRenderer, warningText, width / 2, height / 2 - 20, 16777215);
    }
}
