package com.mcsrleague.mslmod.mixin.loadscreen;

import com.mcsrleague.mslmod.MSLMod;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.WorldGenerationProgressTracker;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");
    @Shadow
    @Final
    private WorldGenerationProgressTracker progressProvider;
    @Shadow
    private long field_19101;

    protected LevelLoadingScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void renderMixin(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (MSLMod.ooml()) {
            mslRender(matrices, mouseX, mouseY, delta);
            info.cancel();
        }
    }

    private void mslRender(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        fill(matrices, 0, 0, width, height, -15724528);
        assert client != null;
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);
        String string = MathHelper.clamp(this.progressProvider.getProgressPercentage(), 0, 100) + "%";
        long l = Util.getMeasuringTimeMs();
        if (l - this.field_19101 > 2000L) {
            this.field_19101 = l;
            NarratorManager.INSTANCE.narrate((new TranslatableText("narrator.loading", new Object[]{string})).getString());
        }
        int i = this.width / 2;
        int j = this.height / 2;
        LevelLoadingScreen.drawChunkMap(matrices, this.progressProvider, i, j + 30, 2, 0);
        TextRenderer var10002 = this.textRenderer;
        this.drawCenteredString(matrices, var10002, string, i, j - 9 / 2 - 30, 16777215);
    }
}
