package me.wolfyscript.timber.configs;

import me.wolfyscript.timber.WolfyTimber;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class TimberConfig extends Config {

    public TimberConfig(ConfigAPI configAPI) {
        super(configAPI, WolfyTimber.getInstance().getDataFolder().getPath(), "main_config", "me/wolfyscript/timber/configs", "config", "yml", false);
    }

    public String getLang(){
        return getString("language");
    }

    public boolean isDebug(){
        return getBoolean("debug");
    }

    public boolean cutLeaves(){
        return getBoolean("timber.cutLeaves");
    }

    public boolean woodSensitiv(){
        return getBoolean("timber.woodSensitive");
    }

    public int maxBlocks(){
        return getInt("timber.max_blocks");
    }

    public int bypassMaxBlocks(){
        return getInt("timber.bypass_maxBlocks");
    }

    public List<Material> getEnabledVanillaAxes(){
        List<Material> list = new ArrayList<>();
        getStringList("vanilla_axes.enabled").forEach(s -> {
            Material material = Material.matchMaterial(s);
            if(material != null){
                list.add(material);
            }
        });
        return list;
    }
}
