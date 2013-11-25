package io.github.dsh105.vortex.environment.wind;

import io.github.dsh105.vortex.environment.Environment;


public class Wind extends Environment {

    public Wind(int liveTime) {
        super(liveTime);
    }

    @Override
    public void onLive() {
        super.onLive();

        if (this.age >= this.liveTime) {
            this.end();
        }
    }
}