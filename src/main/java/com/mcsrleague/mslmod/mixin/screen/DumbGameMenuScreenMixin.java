package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.screen.DumbGameScreen;
import com.mcsrleague.mslmod.widget.UnselectableButtonWidget;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(GameMenuScreen.class)
public abstract class DumbGameMenuScreenMixin extends Screen {
    private UnselectableButtonWidget randomButton;
    private UnselectableButtonWidget cancelToasterButton;
    private int randomButtonPresses;

    protected DumbGameMenuScreenMixin(Text title) {
        super(title);
    }

    private int[] generateRandomButtonPos() {
        Random random = new Random();
        int x;
        int y;
        int attempts = 100;
        do {
            x = random.nextInt(width - 20);
            y = random.nextInt(height - 20);
            attempts += -1;
        } while ((((x + 20) > (width / 2 - 102) && x < (width / 2 + 102) && (y + 20) > (height / 4 + 8) && (y < (height / 4 + 124))) || (x < 70 && (y + 40) < height)) && attempts > 0);
        if (attempts == 0) {
            return null;
        } else {
            return new int[]{x, y};
        }
    }

    @Inject(method = "initWidgets", at = @At("TAIL"))
    private void addButtonsMixin(CallbackInfo info) {
        if (this.client != null && this.client.isInSingleplayer()) {
            randomButtonPresses = 0;
            int[] rPos;
            try {
                rPos = generateRandomButtonPos();
            } catch (Exception ignored) {
                rPos = null;
            }
            if (rPos != null) {

                randomButton = this.addButton(new UnselectableButtonWidget(rPos[0], rPos[1], 20, 20, LiteralText.EMPTY, (buttonWidget) -> {
                    randomButtonPresses += 1;
                    randomButton.setAlpha(((float) randomButtonPresses) / 5.0f);
                    if (randomButtonPresses >= 5) {
                        cancelToasterButton.active = true;
                        randomButton.active = false;
                        cancelToasterButton.setAlpha(1.0f);
                        cancelToasterButton.setMessage(new LiteralText("Cancel Toaster"));
                    }
                }));
                randomButton.setAlpha(0.0f);

                cancelToasterButton = this.addButton(new UnselectableButtonWidget(width - 94, height - 24, 90, 20, LiteralText.EMPTY, (buttonWidget) -> {
                    this.client.openScreen(new DumbGameScreen(this));
                }));
                cancelToasterButton.active = false;
                cancelToasterButton.setAlpha(0.0f);
            }
        }
    }
}
