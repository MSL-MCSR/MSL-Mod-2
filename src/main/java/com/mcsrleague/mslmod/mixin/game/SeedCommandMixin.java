package com.mcsrleague.mslmod.mixin.game;

import com.mcsrleague.mslmod.session.SessionWorld;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.SeedCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SeedCommand.class)
public abstract class SeedCommandMixin {
    /**
     * @author DuncanRuns
     * @reason Prevent /seed in certain worlds
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandManager.literal("seed").requires((serverCommandSource) -> {
            return !dedicated || serverCommandSource.hasPermissionLevel(2);
        })).executes((commandContext) -> {
            long l = ((ServerCommandSource) commandContext.getSource()).getWorld().getSeed();
            Text text = Texts.bracketed((new LiteralText(String.valueOf(l))).styled((style) -> {
                return style.withColor(Formatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, String.valueOf(l))).setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("chat.copy.click"))).withInsertion(String.valueOf(l));
            }));
            if (SessionWorld.isSessionWorld()) {
                ((ServerCommandSource) commandContext.getSource()).sendFeedback(new TranslatableText("mcsrleague.game.noseed").formatted(Formatting.RED), false);
                return 0;
            } else {
                ((ServerCommandSource) commandContext.getSource()).sendFeedback(new TranslatableText("commands.seed.success", new Object[]{text}), false);
                return (int) l;
            }
        }));
    }
}
