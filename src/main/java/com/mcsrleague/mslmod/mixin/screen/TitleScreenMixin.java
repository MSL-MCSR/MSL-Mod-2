package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.WarningModsUtil;
import com.mcsrleague.mslmod.infograbber.HttpsInfoGrabber;
import com.mcsrleague.mslmod.screen.*;
import com.mcsrleague.mslmod.widget.SquishButtonWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    private static final Identifier OPTIONS = new Identifier("mcsrleague:textures/gui/button/options.png");
    private static final Identifier WEBSITE = new Identifier("mcsrleague:textures/gui/button/website.png");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgetsNormal", at = @At("TAIL"))
    private void changeButtonsMixin(int y, int spacingY, CallbackInfo ci) {
        AbstractButtonWidget realmsButton = null;
        int realmsInd = 0;
        for (AbstractButtonWidget buttonWidget : buttons) {
            if (buttonWidget.getMessage() instanceof TranslatableText) {
                if (((TranslatableText) buttonWidget.getMessage()).getKey().equals("menu.online")) {
                    realmsButton = buttonWidget;
                    break;
                }
            }
        }

        if (realmsButton != null) {
            realmsInd = buttons.indexOf(realmsButton);
            buttons.remove(realmsButton);
            children.remove(realmsButton);
        }
        assert client != null;
        AbstractButtonWidget websiteMSLButton = new TexturedButtonWidget(width / 2 - 100, y + spacingY * 2, 20, 20, 0, 0, 20, WEBSITE, 20, 40, button -> {
            client.openScreen(new OpenWebScreen(new FakeTitleScreen()));

        });

        ButtonWidget playMSLButton = new SquishButtonWidget(width / 2 - 76, y + spacingY * 2, 152, 20, new TranslatableText("mcsrleague.title.play").formatted(Formatting.WHITE).formatted(Formatting.BOLD), button -> {
            client.openScreen(new PlayMSLScreen(new HttpsInfoGrabber("https://mcsrleague.com/api/seed/" + FabricLoader.getInstance().getModContainer("mcsrleague").get().getMetadata().getVersion()), true));
        });
        AbstractButtonWidget optionsMSLButton = new TexturedButtonWidget(width / 2 + 80, y + spacingY * 2, 20, 20, 0, 0, 20, OPTIONS, 20, 40, button -> {
            client.openScreen(new MSLOptionsScreen());
        });

        buttons.add(realmsInd, websiteMSLButton);
        children.add(realmsInd, websiteMSLButton);
        buttons.add(realmsInd + 1, playMSLButton);
        children.add(realmsInd + 1, playMSLButton);
        buttons.add(realmsInd + 2, optionsMSLButton);
        children.add(realmsInd + 2, optionsMSLButton);
        if (WarningModsUtil.hasWarningMods()) {
            if (WarningModsUtil.hasBypass()) {
                playMSLButton.active = false;
            } else {
                client.openScreen(new WarningScreen());
            }
        }
        if (MSLMod.ooml() && ((!WarningModsUtil.hasWarningMods()) || WarningModsUtil.hasBypass())) {
            client.openScreen(new ContinueSessionScreen());
        }
    }
}
