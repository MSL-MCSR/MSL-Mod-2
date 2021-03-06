package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.random.SpeedrunRandomUtil;
import com.mcsrleague.mslmod.session.SeedSession;
import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class CustomSeedScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");

    private final Screen parent;

    private TextFieldWidget worldSeedWidget;
    private TextFieldWidget dropSeedWidget;
    private Text worldSeedText;
    private Text dropSeedText;

    protected CustomSeedScreen(Screen parent) {
        super(new TranslatableText("mcsrleague.seed.title"));
        this.parent = parent;
        worldSeedWidget = null;
        dropSeedWidget = null;

    }

    @Override
    public void tick() {
        worldSeedWidget.tick();
        dropSeedWidget.tick();
    }

    @Override
    protected void init() {
        worldSeedWidget = new TextFieldWidget(textRenderer, width / 2 - 85, height / 2 - 20, 170, 20, worldSeedWidget, LiteralText.EMPTY);
        dropSeedWidget = new TextFieldWidget(textRenderer, width / 2 - 85, height / 2 + 30, 170, 20, dropSeedWidget, LiteralText.EMPTY);
        worldSeedWidget.setMaxLength(64);
        dropSeedWidget.setMaxLength(64);
        children.add(worldSeedWidget);
        children.add(dropSeedWidget);
        this.addButton(new MSLButtonWidget(width / 2 - 85, height / 2 + 80, 80, 20, new TranslatableText("mcsrleague.seed.create"), button -> {
            if (!worldSeedWidget.getText().equals("")) {
                if (dropSeedWidget.getText().equals("")) {
                    String[] rawWords = worldSeedWidget.getText().replace("\n"," ").replace("\t"," ").split(" ");
                    List<String> words = new ArrayList<>();
                    for(String string: rawWords){
                        if(!string.equals("")){
                            words.add(string);
                        }
                    }
                    if(words.size() > 1) {
                        worldSeedWidget.setText(words.get(0));
                        dropSeedWidget.setText(words.get(1));
                    } else {
                        worldSeedWidget.setText(words.get(0));
                        dropSeedWidget.setText(words.get(0));
                    }
                } else {
                    SeedSession.createLevel(worldSeedWidget.getText(), this, false);
                    SpeedrunRandomUtil.setCounts(SpeedrunRandomUtil.stringToSeed(dropSeedWidget.getText()));
                }
            }
        }));
        this.addButton(new MSLButtonWidget(width / 2 + 5, height / 2 + 80, 80, 20, new TranslatableText("mcsrleague.seed.cancel"), button -> {
            onClose();
        }));
        worldSeedText = new TranslatableText("mcsrleague.seed.worldseed");
        dropSeedText = new TranslatableText("mcsrleague.seed.dropseed");
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        worldSeedWidget.render(matrices, mouseX, mouseY, delta);
        dropSeedWidget.render(matrices, mouseX, mouseY, delta);

        drawCenteredText(matrices, textRenderer, worldSeedText, width / 2, height / 2 - 35, 16777215);
        drawCenteredText(matrices, textRenderer, dropSeedText, width / 2, height / 2 + 15, 16777215);


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
