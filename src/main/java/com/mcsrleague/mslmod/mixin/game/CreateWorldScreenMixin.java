package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.MSLMod;
import com.mcsrleague.mslmod.WarningModsUtil;
import com.mcsrleague.mslmod.mixin.access.MoreOptionsDialogAccess;
import com.mcsrleague.mslmod.random.SpeedrunRandomUtil;
import com.mcsrleague.mslmod.session.SessionWorldUtil;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalLong;
import java.util.Random;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {
    @Shadow
    @Final
    public MoreOptionsDialog moreOptionsDialog;

    @Inject(method = "createLevel", at = @At("HEAD"))
    private void createLevelMixin(CallbackInfo ci) {
        SessionWorldUtil.setSessionStart(0);

        TextFieldWidget textFieldWidget = ((MoreOptionsDialogAccess) moreOptionsDialog).getSeedTextField();
        OptionalLong optionalLong = SpeedrunRandomUtil.tryParseLong(textFieldWidget.getText());
        if (!(optionalLong.isPresent() && optionalLong.getAsLong() != 0L)) {
            textFieldWidget.setText(String.valueOf(new Random().nextLong()));
        }
        SpeedrunRandomUtil.setCounts(SpeedrunRandomUtil.stringToSeed(textFieldWidget.getText()));

        // If there is invalid mods, and it is not part of a seed session, set default seed to be cracked
        if (WarningModsUtil.isTm() && (!MSLMod.ooml())) {
            SpeedrunRandomUtil.setCounts(487317577770548373L);
        }
    }
}