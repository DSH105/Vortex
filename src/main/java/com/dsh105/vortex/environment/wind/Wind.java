package com.dsh105.vortex.environment.wind;

import com.dsh105.vortex.environment.Environment;


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