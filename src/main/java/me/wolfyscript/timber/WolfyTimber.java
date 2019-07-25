package me.wolfyscript.timber;

import me.wolfyscript.timber.configs.ConfigHandler;
import me.wolfyscript.utilities.api.WolfyUtilities;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WolfyTimber extends JavaPlugin {

    private static Plugin instance;
    private WolfyUtilities api;
    private ConfigHandler configHandler;

    @Override
    public void onEnable() {
        instance = this;
        api = new WolfyUtilities(instance);
        api.setCHAT_PREFIX("§7[§3WT§7]");
        api.setCONSOLE_PREFIX("§7[§3WT§7]");

        //CONFIGS
        //LANGUAGE LOADING
        configHandler = new ConfigHandler(api);
        configHandler.load();




    }

    @Override
    public void onDisable() {

    }

    public static Plugin getInstance() {
        return instance;
    }

}
