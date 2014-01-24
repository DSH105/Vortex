package com.dsh105.vortex.environment;

import com.dsh105.vortex.VortexPlugin;

public abstract class Environment {

    private EnvironmentTick liveTicker;
    public int age;
    public int liveTime;

    public Environment(int liveTime) {
        this.liveTicker = new EnvironmentTick(this);
        this.liveTime = liveTime;
    }

    public void onLive() {
        this.age++;
    }

    public void start() {
        this.liveTicker.runTaskTimer(VortexPlugin.getInstance(), 1L, 1L);
    }

    public void end() {
        this.liveTicker.cancel();
    }
}