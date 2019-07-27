package me.wolfyscript.timber.handler;

import me.wolfyscript.timber.WolfyTimber;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TimberAxeHandler {

    public static boolean isTimberAxe(ItemStack itemStack){
        if(isCustomTimberAxe(itemStack)){
            return true;
        }else
            return WolfyTimber.getSettings().getEnabledVanillaAxes().contains(itemStack.getType());
    }

    public static boolean isCustomTimberAxe(ItemStack itemStack){
        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()){
            List<String> lore = itemStack.getItemMeta().getLore();
            for(String line : lore){
                if(WolfyUtilities.unhideString(line).equals("wt_timberaxe")){
                    return true;
                }
            }
        }
        return false;
    }

}
