package io.github.dsh105.overcast.environment;

import io.github.dsh105.overcast.Overcast;

public abstract class Environment {

    private EnvironmentTick liveTicker;
    public int age;

    public Environment() {
        this.liveTicker = new EnvironmentTick(this);
    }

    public abstract void onLive();

    public void start() {
        this.liveTicker.runTaskTimer(Overcast.getInstance(), 1L, 1L);
    }

    public void end() {
        this.liveTicker.cancel();
    }
}