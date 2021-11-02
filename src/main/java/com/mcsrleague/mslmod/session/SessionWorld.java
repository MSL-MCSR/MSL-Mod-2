package com.mcsrleague.mslmod.session;

public class SessionWorld {
    private static boolean sw = false;

    public static boolean isSessionWorld() {
        return sw;
    }

    public static void setSessionWorld(boolean sw) {
        SessionWorld.sw = sw;
    }
}
