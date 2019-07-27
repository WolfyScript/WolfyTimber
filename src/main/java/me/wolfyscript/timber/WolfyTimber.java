package me.wolfyscript.timber;

import me.wolfyscript.timber.commands.TimberCommand;
import me.wolfyscript.timber.configs.ConfigHandler;
import me.wolfyscript.timber.configs.TimberConfig;
import me.wolfyscript.timber.listeners.BlockListener;
import me.wolfyscript.utilities.api.WolfyUtilities;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WolfyTimber extends JavaPlugin {

    private static Plugin instance;
    private static WolfyUtilities api;
    private static ConfigHandler configHandler;

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

        Bukkit.getPluginCommand("timber").setExecutor(new TimberCommand());
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
    }

    @Override
    public void onDisable() {

    }

    public static TimberConfig getSettings(){
        return configHandler.getTimberConfig();
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static WolfyUtilities getApi() {
        return api;
    }
}
