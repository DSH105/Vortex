package io.github.dsh105.overcast.config.options;

import io.github.dsh105.overcast.config.YAMLConfig;

/**
 * Project by DSH105
 */

public class ConfigOptions extends Options {

    public static ConfigOptions instance;

    public ConfigOptions(YAMLConfig config) {
        super(config);
        instance = this;
        this.setDefaults();
    }

    @Override
    public void setDefaults() {



        config.saveConfig();
    }
}