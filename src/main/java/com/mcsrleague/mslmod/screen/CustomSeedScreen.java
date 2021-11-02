package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.random.SpeedrunRandomHelper;
import com.mcsrleague.mslmod.session.SeedSession;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.OptionalLong;

public class CustomSeedScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    private static final Identifier CREATE = new Identifier("mcsrleague:textures/gui/button/create.png");
    private static final Identifier CANCEL = new Identifier("mcsrleague:textures/gui/button/cancel.png");

    private final Screen parent;

    private TextFieldWidget worldSeedWidget;
    private TextFieldWidget dropSeedWidget;

    protected CustomSeedScreen(Screen parent) {
        super(new LiteralText("Play Custom Seed"));
        this.parent = parent;
        worldSeedWidget = null;
        dropSeedWidget = null;

    }

    private static long stringToSeed(String string) {

        OptionalLong optionalLong4;
        if (StringUtils.isEmpty(string)) {
            optionalLong4 = OptionalLong.empty();
        } else {
            OptionalLong optionalLong2 = tryParseLong(string);
            if (optionalLong2.isPresent() && optionalLong2.getAsLong() != 0L) {
                optionalLong4 = optionalLong2;
            } else {
                optionalLong4 = OptionalLong.of((long) string.hashCode());
            }
        }

        if (optionalLong4.isPresent()) {
            return optionalLong4.getAsLong();
        } else {
            return 0L;
        }

    }

    private static OptionalLong tryParseLong(String string) {
        try {
            return OptionalLong.of(Long.parseLong(string));
        } catch (NumberFormatException var2) {
            return OptionalLong.empty();
        }
    }

    @Override
    public void tick() {
        worldSeedWidget.tick();
        dropSeedWidget.tick();
    }

    @Override
    protected void init() {
        worldSeedWidget = new TextFieldWidget(textRenderer, width / 2 - 85, height / 2 - 20, 170, 20, worldSeedWidget, new LiteralText(""));
        dropSeedWidget = new TextFieldWidget(textRenderer, width / 2 - 85, height / 2 + 30, 170, 20, dropSeedWidget, new LiteralText(""));
        children.add(worldSeedWidget);
        children.add(dropSeedWidget);
        this.addButton(new TexturedButtonWidget(width / 2 - 85, height / 2 + 80, 80, 20, 0, 0, 20, CREATE, 80, 40, button -> {
            if (!worldSeedWidget.getText().equals("")) {
                if (dropSeedWidget.getText().equals("")) {
                    dropSeedWidget.setText(worldSeedWidget.getText());
                } else {
                    SpeedrunRandomHelper.setOverride(stringToSeed(dropSeedWidget.getText()));
                    SeedSession.createLevel(worldSeedWidget.getText(), this);
                }
            }

        }));
        this.addButton(new TexturedButtonWidget(width / 2 + 5, height / 2 + 80, 80, 20, 0, 0, 20, CANCEL, 80, 40, button -> {
            onClose();
        }));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        worldSeedWidget.render(matrices, mouseX, mouseY, delta);
        dropSeedWidget.render(matrices, mouseX, mouseY, delta);

        drawCenteredString(matrices, textRenderer, "World Seed", width / 2, height / 2 - 35, 16777215);
        drawCenteredString(matrices, textRenderer, "Drop Seed (Barters, etc.)", width / 2, height / 2 + 15, 16777215);


        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void onClose() {
        assert client != null;
        client.openScreen(parent);
    }
}
