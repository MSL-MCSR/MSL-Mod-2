package com.mcsrleague.mslmod.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PickableTexturedButtonWidget extends TexturedButtonWidget implements PickableWidget {
    private final int u;
    private final int v;
    private final Identifier texture;
    private final int hoveredVOffset;
    private final int textureWidth;
    private final int textureHeight;
    private boolean picked;

    public PickableTexturedButtonWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, PressAction pressAction) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction);
        this.u = u;
        this.v = v;
        this.texture = texture;
        this.hoveredVOffset = hoveredVOffset;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getTextureManager().bindTexture(this.texture);
        int i = this.v;
        if (picked || this.isHovered()) {
            i += this.hoveredVOffset;
        }

        RenderSystem.enableDepthTest();
        drawTexture(matrices, this.x, this.y, (float) this.u, (float) i, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
