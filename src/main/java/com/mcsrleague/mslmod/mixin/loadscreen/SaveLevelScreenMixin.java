package com.mcsrleague.mslmod.mixin.loadscreen;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaveLevelScreen.class)
public abstract class SaveLevelScreenMixin extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");

    protected SaveLevelScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderMixin(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (MSLMod.ooml()) {
            mslRender(matrices);
            info.cancel();
        }
    }

    private void mslRender(MatrixStack matrices) {
        fill(matrices, 0, 0, width, height, -15724528);
        assert client != null;
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        this.drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, height / 2 - 48, 16777215);
    }
}
