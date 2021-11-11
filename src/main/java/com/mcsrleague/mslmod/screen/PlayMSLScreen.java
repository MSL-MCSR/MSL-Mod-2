package com.mcsrleague.mslmod.screen;

import com.mcsrleague.mslmod.infograbber.GrabbedInfoJson;
import com.mcsrleague.mslmod.infograbber.InfoGrabber;
import com.mcsrleague.mslmod.mixin.access.MinecraftClientAccess;
import com.mcsrleague.mslmod.random.SpeedrunRandomHelper;
import com.mcsrleague.mslmod.session.SeedSession;
import com.mcsrleague.mslmod.widget.MSLButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.Util;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class PlayMSLScreen extends Screen {
    private static final Identifier MSL_LOGO = new Identifier("mcsrleague:textures/gui/msl_logo.png");

    private final InfoGrabber infoGrabber;
    private final Text escWarnText;
    private Text grabText;
    private Text countdownText;
    private int stage;
    private String worldSeed;
    private String dropSeed;
    private boolean hasDropSeed;
    private long seedStart;
    private int timeLeft;
    private long nextGrab;
    private boolean closed;
    private int color;
    private long pressedEsc;
    private boolean hasDownloadButton;
    private ButtonWidget cancelButton;

    private List<Text> errorTexts;
    private int errorOffset;
    private String errorMessage;

    public PlayMSLScreen(InfoGrabber infoGrabber) {
        super(new TranslatableText("mcsrleague.title.play"));
        this.infoGrabber = infoGrabber;
        grabText = LiteralText.EMPTY;
        countdownText = new TranslatableText("mcsrleague.play.waiting");
        stage = 0;
        closed = false;
        color = 16777215;
        pressedEsc = 0;
        hasDownloadButton = false;
        escWarnText = new TranslatableText("mcsrleague.play.escwarn");
    }


    private static String getTimeLeftString(int timeLeft) {
        if (timeLeft >= 60) {
            if (timeLeft >= 3600) {
                int secondsMH = timeLeft % 3600;
                int hours = timeLeft / 3600;
                int seconds = secondsMH % 60;
                int minutes = secondsMH / 60;
                String secondsString = ensureSize2(String.valueOf(seconds));
                String minutesString = ensureSize2(String.valueOf(minutes));
                return hours + ":" + minutesString + ":" + secondsString;
            } else {
                int seconds = timeLeft % 60;
                int minutes = timeLeft / 60;
                String secondsString = ensureSize2(String.valueOf(seconds));
                return minutes + ":" + secondsString;
            }
        } else {
            return String.valueOf(timeLeft);
        }
    }

    private static String ensureSize2(String string) {
        return string.length() == 1 ? "0" + string : string;

    }

    private static OptionalLong tryParseLong(String string) {
        try {
            return OptionalLong.of(Long.parseLong(string));
        } catch (NumberFormatException var2) {
            return OptionalLong.empty();
        }
    }

    private static long stringToSeed(String string) {

        OptionalLong optionalLong4;
        if (StringUtils.isEmpty(string)) {
            optionalLong4 = OptionalLong.empty();
        } else {
            OptionalLong optionalLong2 = tryParseLong(string);
            if (optionalLong2.isPresent() && optionalLong2.getAsLong() != 0L) {
                optionalLong4 = optionalLong2;
            } else {
                optionalLong4 = OptionalLong.of(string.hashCode());
            }
        }

        if (optionalLong4.isPresent()) {
            return optionalLong4.getAsLong();
        } else {
            return 0L;
        }

    }

    private Identifier getNumberImageID(int i) {
        if (i > 0 && i <= 10) {
            return new Identifier("mcsrleague:textures/gui/number/n" + i + ".png");
        }
        return null;
    }

    private int getTimeLeft() {
        return (int) Math.ceil((seedStart - System.currentTimeMillis()) / 1000.0F);
    }

    private void forceRender() {
        if (client != null) {
            ((MinecraftClientAccess) client).invokeRender(false);
        }
    }

    private GrabbedInfoJson grab() {
        GrabbedInfoJson grabbedInfo = null;
        for (int tries = 0; tries < 10; tries++) {
            if (closed) {
                break;
            }
            grabText = new TranslatableText("mcsrleague.play.retreiving").append(new LiteralText(" (")).append(new TranslatableText("mcsrleague.play.try")).append(new LiteralText(" " + (tries + 1) + "/10)"));
            forceRender();
            grabbedInfo = infoGrabber.fullGrab();
            if (grabbedInfo != null) {
                break;
            }
        }
        grabText = new LiteralText("");
        return grabbedInfo;
    }

    @Override
    protected void init() {
        if (stage == 100) {
            if (hasDownloadButton) {
                addButton(new MSLButtonWidget(width / 2 - 40, height / 2 + 50, 80, 20, new TranslatableText("mcsrleague.play.download"), button -> {
                    Util.getOperatingSystem().open("https://mcsrleague.com/api/mod/download/");
                }));
            } else {
                addButton(new MSLButtonWidget(width / 2 - 40, height - 25, 80, 20, new TranslatableText("mcsrleague.play.copyerror"), button -> {
                    assert client != null;
                    client.keyboard.setClipboard(errorMessage);
                }));
            }
        }
        cancelButton = addButton(new MSLButtonWidget(width - 84, height - 24, 80, 20, new TranslatableText("mcsrleague.play.cancel"), button -> onClose()));
    }

    @Override
    public void tick() {
        if (stage > 0 && stage < 100) {
            timeLeft = getTimeLeft();
            countdownText = timeLeft <= 10 && timeLeft > 0 ? new TranslatableText("mcsrleague.play.soon") : new TranslatableText("mcsrleague.play.starting").append(new LiteralText(" " + getTimeLeftString(timeLeft) + "..."));

        } else if (stage == 100) {
            countdownText = new TranslatableText("mcsrleague.play.fail");
            color = 16737380;
        }
        switch (stage) {
            case 0:
                grabAndSetInfo();
                break;
            case 1:
                waitForSeedStage();
                break;
            case 2:
                waitForStartStage();
                break;
        }
    }

    private void waitForSeedStage() {
        if (System.currentTimeMillis() > nextGrab) {
            grabAndSetInfo();
        }
    }

    private void grabAndSetInfo() {
        GrabbedInfoJson info = grab();
        if (info != null) {
            seedStart = System.currentTimeMillis() + (long) (info.countdown * 1000);
            if (info.seedCountdown != null) {
                if (info.seedCountdown < 120) {
                    double add = 0;
                    double dif = info.countdown - info.seedCountdown;
                    if (dif > 10.0D) {
                        Random random = new Random();
                        add = random.nextDouble() * (dif - 10D);
                    }
                    nextGrab = System.currentTimeMillis() + ((long) ((info.seedCountdown + add) * 1000));
                } else {
                    nextGrab = System.currentTimeMillis() + 90000;
                }
                stage = 1;
            } else {
                worldSeed = info.seed;
                stage = 2;
                if (info.dropSeed != null) {
                    dropSeed = info.dropSeed;
                    hasDropSeed = true;
                }
            }
        } else {
            stage = 100;
            setErrorTexts();
            init();
        }
    }

    private Text toErrorText(String string) {
        boolean big = false;
        while (textRenderer.getWidth(string) > 400) {
            big = true;
            string = string.substring(0, string.length() - 1);
        }
        if (big) {
            string = string.substring(0, string.length() - 3) + "...";
        }
        return new LiteralText(string);
    }

    private void setErrorTexts() {
        String message = infoGrabber.getError();
        if (message == null) {
            message = "Error is null";
        }

        if (message.startsWith("OOD ")) {
            String version = message.replace("OOD ", "");
            message = Language.getInstance().get("mcsrleague.play.ood") + "\n" + Language.getInstance().get("mcsrleague.play.downloadmsg") + " (" + version + ").";
            hasDownloadButton = true;
        }

        errorMessage = message;

        int max = 0;
        List<Text> textList = new ArrayList<>();
        int totalLines = 0;
        List<String> stringList = Arrays.asList(message.split("\n"));
        for (String line : stringList) {
            totalLines++;
            Text text;
            if (totalLines == 10) {
                int linesLeft = stringList.size() - 9;
                text = new LiteralText("+" + linesLeft + " more line" + (linesLeft == 1 ? "" : "s"));
            } else {
                text = toErrorText(line);
            }
            int textWidth = textRenderer.getWidth(text);
            if (textWidth > max) {
                max = textWidth;
            }
            textList.add(text);
            if (totalLines == 10) {
                break;
            }
        }
        errorOffset = max / 2;
        errorTexts = textList;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        assert client != null;
        fill(matrices, 0, 0, width, height, -15724528);
        drawCenteredText(matrices, textRenderer, countdownText, width / 2, height / 2 - 48, color);
        drawCenteredText(matrices, textRenderer, grabText, width / 2, height - 15, 16777215);
        if (stage == 100) {
            renderError(matrices);
        }

        super.render(matrices, mouseX, mouseY, delta);
        client.getTextureManager().bindTexture(MSL_LOGO);
        drawTexture(matrices, this.width / 2 - 64, height / 2 - 112, 0.0F, 0.0F, 128, 64, 128, 64);

        if (stage < 100 && stage > 0 && timeLeft <= 10 && timeLeft > 0) {
            client.getTextureManager().bindTexture(getNumberImageID(timeLeft));
            drawTexture(matrices, width / 2 - 32, height / 2 - 32, 0.0F, 0.0F, 64, 64, 64, 64);
        }

        if (pressedEsc - System.currentTimeMillis() > 0) {
            drawCenteredText(matrices, textRenderer, escWarnText, cancelButton.x + cancelButton.getWidth() / 2, cancelButton.y - 10, 16777215);
        }
    }

    private void renderError(MatrixStack matrices) {
        int y = height / 2;
        fill(matrices, width / 2 - errorOffset - 5, y + 5, width / 2 + errorOffset + 5, y + 10 * errorTexts.size() + 14, -16777216);
        for (Text text : errorTexts) {
            y += 10;
            textRenderer.drawTrimmed(text, width / 2 - errorOffset, y, 900, color);
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        closed = true;
    }

    private void waitForStartStage() {
        if (System.currentTimeMillis() > seedStart) {
            if (hasDropSeed) {
                SpeedrunRandomHelper.setOverride(stringToSeed(dropSeed));
            }
            SeedSession.createLevel(worldSeed, this);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            pressedEsc = System.currentTimeMillis() + 2500;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}
