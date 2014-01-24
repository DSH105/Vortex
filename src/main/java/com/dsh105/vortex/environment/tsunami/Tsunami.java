package com.dsh105.vortex.environment.tsunami;

import com.dsh105.vortex.environment.Environment;


public class Tsunami extends Environment {

    public Tsunami(int liveTime) {
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