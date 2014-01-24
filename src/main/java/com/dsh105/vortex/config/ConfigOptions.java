package com.dsh105.vortex.config;

import io.github.dsh105.dshutils.config.YAMLConfig;
import io.github.dsh105.dshutils.config.options.Options;

public class ConfigOptions extends Options {

    public static ConfigOptions instance;

    public ConfigOptions(YAMLConfig config) {
        super(config);
        instance = this;
    }

    @Override
    public void setDefaults() {

        config.saveConfig();
    }
}