package com.mcsrleague.mslmod.infograbber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestSeedInfoGrabber extends InfoGrabber {

    private boolean isFast;

    public TestSeedInfoGrabber(boolean isFast) {
        this.isFast = isFast;
    }

    protected GrabbedInfoJson randomTestSeed() {
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
        seeds.add("62517625669455919;-6478871361211071306");
        seeds.add("8816993650241934;3995250007797399150");
        seeds.add("2286662245080230924;-2500092126412403435");
        seeds.add("18351931606909802;3677087581936703429");
        seeds.add("38299236018892713;2243456438900695378");
        seeds.add("106218965707435300;8589198316243376547");
        seeds.add("-8420086534322146032;-3416761803112979907");
        seeds.add("3050774164590357;5176474541024317041");
        seeds.add("661194789352588716;-3514231930886522777");
        seeds.add("1833883051935046631;-6838524497813864137");
        seeds.add("16485807253397467;-8501238572094208408");
        String selected = seeds.get(random.nextInt(seeds.size()));
        String seed;
        String dropSeed = null;
        if (selected.contains(";")) {
            String[] split = selected.split(";");
            seed = split[0];
            dropSeed = split[1];
        } else {
            seed = selected;
        }
        GrabbedInfoJson grabbedInfoJson = new GrabbedInfoJson();
        grabbedInfoJson.seed = seed;
        grabbedInfoJson.dropSeed = dropSeed;
        grabbedInfoJson.countdown = isFast ? 0.0d : 5.0d;
        return grabbedInfoJson;
    }

    @Override
    public GrabbedInfoJson fullGrab() {
        return randomTestSeed();
    }

    @Override
    public String grab() {
        return "";
    }

    @Override
    public String getError() {
        return "FakeInfoGrabber Error, you shouldn't be seeing this but you are I guess.";
    }


}
