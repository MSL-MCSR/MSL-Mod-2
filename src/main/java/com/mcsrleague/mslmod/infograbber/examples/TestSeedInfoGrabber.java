package com.mcsrleague.mslmod.infograbber.examples;

import com.mcsrleague.mslmod.infograbber.InfoGrabber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestSeedInfoGrabber extends InfoGrabber {

    private int grabCount;

    public TestSeedInfoGrabber() {
        grabCount = 0;
    }

    protected String randomTestSeed() {
        Random random = new Random();
        List<String> seeds = new ArrayList<>();
        seeds.add("2925709559301942");
        seeds.add("8186677555369476");
        seeds.add("798443536342809");
        seeds.add("15227624738481789");
        seeds.add("24430766566782136");
        seeds.add("10608500664789064");
        seeds.add("1031960268574729");
        seeds.add("41535343890503663");
        seeds.add("14053836293512989");
        seeds.add("24573906965512686");
        seeds.add("7665564001459390");
        seeds.add("94750899065580431");
        seeds.add("339984622913977153");
        seeds.add("334621522243804560");
        seeds.add("209081777557304284");
        seeds.add("29831551122935987");
        seeds.add("19327131959141809");
        seeds.add("6932345740285412");
        seeds.add("21394814659703036");
        seeds.add("8710950998499630059");
        seeds.add("192963560563794841");
        seeds.add("107468853166784729");
        seeds.add("138113256244534520");
        seeds.add("997550319034672541");
        return seeds.get(random.nextInt(seeds.size()));
    }

    @Override
    public String grab() {
        grabCount++;
        switch (grabCount) {
            case 1:
                return "{\n" +
                        "  \"countdown\": 15,\n" +
                        "  \"seedCountdown\": 4\n" +
                        "}";
            case 2:
                return "{\n" +
                        "  \"countdown\": 10.6,\n" +
                        "  \"seed\": \"" + randomTestSeed() + "\"\n" +
                        "}";
        }
        return null;
    }

    @Override
    public String getError() {
        return "FakeInfoGrabber Error, you shouldn't be seeing this but you are I guess.";
    }


}
