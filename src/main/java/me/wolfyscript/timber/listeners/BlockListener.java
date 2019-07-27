package me.wolfyscript.timber.listeners;

import me.wolfyscript.timber.Timber;
import me.wolfyscript.timber.WolfyTimber;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class BlockListener implements Listener {

    private HashMap<UUID, Timber> timbers = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
        Block current = e.getBlock();
        Player p = e.getPlayer();
        ItemStack is = p.getInventory().getItemInMainHand();
        if (is.getType().equals(Material.DIAMOND_AXE)) {
            if (current.getType().toString().endsWith("_LOG") && !e.getPlayer().isSneaking()) {
                if(!timbers.containsKey(p.getUniqueId())){
                    Timber timber = new Timber(current, p);
                    timbers.put(p.getUniqueId(), timber);
                    timber.breakTree();
                }else if(!timbers.get(p.getUniqueId()).isRunning()){
                    timbers.get(p.getUniqueId()).setNewObjective(current);
                    timbers.get(p.getUniqueId()).breakTree();
                }else{
                    WolfyTimber.getApi().sendPlayerMessage(p, "§cYou need to wait till the previous one is done!");
                }
            }
        }
    }
}
