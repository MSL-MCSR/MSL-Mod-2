package com.mcsrleague.mslmod.mixin.screen;

import com.mcsrleague.mslmod.mixin.access.ButtonWidgetAccess;
import com.mcsrleague.mslmod.screen.SimplePromptScreen;
import com.mcsrleague.mslmod.session.SessionWorldUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectWorldScreen.class)
public abstract class SelectWorldScreenMixin extends Screen {
    @Shadow
    private ButtonWidget recreateButton;

    @Shadow
    private WorldListWidget levelList;

    @Shadow
    private ButtonWidget editButton;

    protected SelectWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initMixin(CallbackInfo info) {
        ((ButtonWidgetAccess) recreateButton).setOnPress(button -> {
            this.levelList.method_20159().ifPresent(WorldListWidget.Entry::recreate);
            if (SessionWorldUtil.isSessionWorld()) {
                client.openScreen(new SimplePromptScreen(this, new TranslatableText("mcsrleague.warning.raceworld"), new TranslatableText("mcsrleague.warning.ok"), true));
            }
        });

        ((ButtonWidgetAccess) editButton).setOnPress(button -> {
            this.levelList.method_20159().ifPresent(WorldListWidget.Entry::recreate);
            if (SessionWorldUtil.isSessionWorld()) {
                client.openScreen(new SimplePromptScreen(this, new TranslatableText("mcsrleague.warning.raceworld"), new TranslatableText("mcsrleague.warning.ok"), true));
            } else {
                this.levelList.method_20159().ifPresent(WorldListWidget.Entry::edit);
            }
        });
    }
}
