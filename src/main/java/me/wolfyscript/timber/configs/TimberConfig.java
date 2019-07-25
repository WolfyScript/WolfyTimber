package me.wolfyscript.timber.configs;

import me.wolfyscript.timber.WolfyTimber;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;

public class TimberConfig extends Config {

    public TimberConfig(ConfigAPI configAPI) {
        super(configAPI, "me/wolfyscript/timber/configs", WolfyTimber.getInstance().getDataFolder().getPath(), "config");
    }

    public String getLang(){
        return getString("language");
    }
}
