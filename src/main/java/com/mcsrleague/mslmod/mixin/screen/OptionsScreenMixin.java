package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.DumbUtil;
import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.screen.DumbGameScreen;
import com.mcsrleague.mslmod.widget.SquishButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Shadow private ButtonWidget difficultyButton;

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo info) {
        assert this.client != null;
        if (DumbUtil.isDumbUnlocked()) {
            addButton(new SquishButtonWidget(4, height - 24, 90, 20, new TranslatableText("mcsrleague.dumb.canceltoaster"), button -> {
                this.client.openScreen(new DumbGameScreen(this));
            }));
        }
    }

    @Inject(method="getDifficultyButtonText",at=@At("HEAD"),cancellable = true)
    private void getDifficultyButtonTextMixin(Difficulty difficulty, CallbackInfoReturnable<Text> info){
        if(MSLMod.ooml() &&  difficulty.equals(Difficulty.PEACEFUL)){
            info.setReturnValue((new TranslatableText("options.difficulty")).append(": ").append(((TranslatableText)difficulty.getTranslatableName()).formatted(Formatting.STRIKETHROUGH)).append(Difficulty.EASY.getTranslatableName()));
        }
    }
}
