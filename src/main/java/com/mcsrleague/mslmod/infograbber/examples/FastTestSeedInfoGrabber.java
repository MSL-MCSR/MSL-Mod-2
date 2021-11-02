package com.mcsrleague.mslmod.infograbber.examples;

public class FastTestSeedInfoGrabber extends TestSeedInfoGrabber {

    public FastTestSeedInfoGrabber() {
    }

    @Override
    public String grab() {
        return "{\n" +
                "  \"countdown\": 0,\n" +
                "  \"seed\": \"" + randomTestSeed() + "\"\n" +
                "}";
    }

    @Override
    public String getError() {
        return "FakeInfoGrabber Error, you shouldn't be seeing this but you are I guess.";
    }


}
