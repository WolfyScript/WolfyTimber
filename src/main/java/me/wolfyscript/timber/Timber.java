package me.wolfyscript.timber;

import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Timber {

    private Block startBlock;
    private Material startBlockMaterial;
    private UUID uuid;
    private Player player;
    private ItemStack tool;
    private Material leavesType;
    private BukkitTask task;

    public Timber(Block startBlock, Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        setNewObjective(startBlock);
        this.tool = player.getInventory().getItemInMainHand();
    }

    public void setNewObjective(Block startBlock){
        this.player = Bukkit.getPlayer(uuid);
        if(player != null){
            this.tool = player.getInventory().getItemInMainHand();
            this.startBlock = startBlock;
            this.startBlockMaterial = startBlock.getType();
            this.leavesType = Material.valueOf(startBlock.getType().toString().replace("_LOG", "_LEAVES").replace("STRIPPED_", ""));
        }
    }

    private boolean isWoodCorrect(Block blockToCheck) {
        return blockToCheck.getType() == this.startBlockMaterial;
    }

    private boolean areLeavesCorrect(Block leaves) {
        return WolfyTimber.getSettings().cutLeaves() && leaves.getType() == leavesType;
    }

    public void breakTree() {
        int maxBlocks = WolfyUtilities.hasPermission(player, "wolfytimber.limit_bypass") ? WolfyTimber.getSettings().bypassMaxBlocks() : WolfyTimber.getSettings().maxBlocks();
        this.task = new BukkitRunnable(){
            @Override
            public void run() {
                ArrayList<Block> totalBlocks = new ArrayList<>();
                ArrayList<Block> blocksUpdate = new ArrayList<>();
                ArrayList<Block> blocksNext = new ArrayList<>();
                blocksNext.add(startBlock);
                int roundFoundLogs;
                do {
                    roundFoundLogs = 0;
                    for (Block block : blocksNext) {
                        for (int x = -1; x < 2; x++) {
                            for (int y = 0; y < 2; y++) {
                                for (int z = -1; z < 2; z++) {
                                    Block next = block.getLocation().clone().add(x, y,z).getBlock();
                                    if (!totalBlocks.contains(next)) {
                                        if (isWoodCorrect(next)) {
                                            blocksUpdate.add(next);
                                            totalBlocks.add(next);
                                            breakBlock(next);
                                            roundFoundLogs++;
                                        } else if (areLeavesCorrect(next)) {
                                            totalBlocks.add(next);
                                            breakBlock(next);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    blocksNext.clear();
                    if (!blocksUpdate.isEmpty()) {
                        blocksNext.addAll(blocksUpdate);
                    }
                    blocksUpdate.clear();
                } while (roundFoundLogs > 0 && totalBlocks.size() < maxBlocks);

                if(player.getGameMode().equals(GameMode.SURVIVAL)){
                    if(ItemUtils.hasCustomDurability(tool)){
                        ItemUtils.setDamage(tool, ItemUtils.getDamage(tool) + totalBlocks.size());
                    }else{
                        ItemMeta itemMeta = tool.getItemMeta();
                        if(((Damageable) itemMeta).getDamage() > 0){
                            ((Damageable) itemMeta).setDamage(((Damageable) itemMeta).getDamage() + totalBlocks.size());
                            tool.setItemMeta(itemMeta);
                        }
                    }
                }
                player.updateInventory();
                task.cancel();
            }
        }.runTaskAsynchronously(WolfyTimber.getInstance());
    }

    private void breakBlock(Block block){
        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
            Bukkit.getScheduler().runTaskLater(WolfyTimber.getInstance(), (Runnable) block::breakNaturally, 2);
        } else {
            Bukkit.getScheduler().runTaskLater(WolfyTimber.getInstance(), () -> block.setType(Material.AIR), 1);
        }
    }

    public boolean isRunning(){
        return !task.isCancelled();
    }
}
