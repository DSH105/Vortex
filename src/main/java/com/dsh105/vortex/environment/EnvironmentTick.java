package com.dsh105.vortex.environment;

import org.bukkit.scheduler.BukkitRunnable;

public class EnvironmentTick extends BukkitRunnable {

    protected Environment environment;

    public EnvironmentTick(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void run() {
        this.environment.onLive();
    }
}