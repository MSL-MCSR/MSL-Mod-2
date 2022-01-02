package com.mcsrleague.mslmod.session;

public abstract class SessionWorldUtil {
    private static long sessionStart = 0L;


    public static long getSessionStart() {
        return sessionStart;
    }

    public static void setSessionStart(long time) {
        sessionStart = time;
    }

    public static void setSessionStart() {
        setSessionStart(System.currentTimeMillis());
    }

    public static boolean isSessionWorld() {
        long dif = System.currentTimeMillis() - sessionStart;
        return dif <= 1800000;
    }
}
