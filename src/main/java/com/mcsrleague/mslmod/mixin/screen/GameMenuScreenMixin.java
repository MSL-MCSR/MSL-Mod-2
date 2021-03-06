package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.screen.ExitOptionsScreen;
import com.mcsrleague.mslmod.session.SessionWorldUtil;
import com.mcsrleague.mslmod.widget.SquishButtonWidget;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
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
        if (showMenu && MSLMod.ooml()) {
            assert client != null;
            assert sqButton != null;
            buttons.remove(sqButton);
            children.remove(sqButton);
            addButton(new SquishButtonWidget(sqButton.x, sqButton.y, sqButton.getWidth(), sqButton.getHeight(), new TranslatableText("mcsrleague.game.exit"), button -> {
                client.openScreen(new ExitOptionsScreen(this));
            }));
        }
        if(showMenu && (MSLMod.ooml() || SessionWorldUtil.isSessionWorld())){
            assert lanButton != null;
            lanButton.active = false;
        }

    }
}
