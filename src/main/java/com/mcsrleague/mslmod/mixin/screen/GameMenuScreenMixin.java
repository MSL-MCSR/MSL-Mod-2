package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.screen.ExitOptionsScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    @Shadow
    @Final
    private boolean showMenu;

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo info) {
        if (showMenu && MSLMod.ooml()) {
            assert client != null;
            AbstractButtonWidget sqButton = null;
            AbstractButtonWidget lanButton = null;
            for (AbstractButtonWidget buttonWidget : buttons) {
                if (buttonWidget.getMessage() instanceof TranslatableText) {
                    if (((TranslatableText) buttonWidget.getMessage()).getKey().equals("menu.shareToLan")) {
                        lanButton = buttonWidget;
                    } else if (((TranslatableText) buttonWidget.getMessage()).getKey().equals("menu.returnToMenu")) {
                        sqButton = buttonWidget;
                    }
                }
            }

            assert lanButton != null && sqButton != null;
            lanButton.active = false;
            buttons.remove(sqButton);
            children.remove(sqButton);
            addButton(new ButtonWidget(sqButton.x, sqButton.y, sqButton.getWidth(), sqButton.getHeight(), new TranslatableText("mcsrleague.game.exit"), button -> {
                client.openScreen(new ExitOptionsScreen(this));
            }));
        }
    }
}
