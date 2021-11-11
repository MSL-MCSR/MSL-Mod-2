package com.mcsrleague.mslmod.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class SquishButtonWidget extends ButtonWidget {

    public SquishButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        minecraftClient.getTextureManager().bindTexture(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(matrices, minecraftClient, mouseX, mouseY);
        int tWidth = textRenderer.getWidth(getMessage());
        int maxWidth = width - 2;
        float scale = 1.0f;
        if (tWidth > maxWidth) {
            scale = (maxWidth) / (float) tWidth;
            matrices.scale(scale, 1, 1);
            tWidth = maxWidth;
        }
        int j = this.active ? 16777215 : 10526880;
        textRenderer.drawWithShadow(matrices, getMessage(), (x + (width - tWidth) / 2) / scale, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
        matrices.scale(1 / scale, 1, 1);
    }
}
