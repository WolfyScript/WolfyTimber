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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Timber {

    private Block startBlock;
    private Material startBlockMaterial;
    private Player player;
    private ItemStack tool;
    private Material leavesType;
    private BukkitTask task;

    public Timber(Block startBlock, Player player) {
        setNewObjective(startBlock);
        this.player = player;
        this.tool = player.getInventory().getItemInMainHand();
    }

    public void setNewObjective(Block startBlock){
        this.startBlock = startBlock;
        this.startBlockMaterial = startBlock.getType();
        this.leavesType = Material.valueOf(startBlock.getType().toString().replace("_LOG", "_LEAVES").replace("STRIPPED_", ""));
    }

    private boolean isWoodCorrect(Block blockToCheck) {
        return blockToCheck.getType() == this.startBlockMaterial;
    }

    private boolean areLeavesCorrect(Block leaves) {
        return WolfyTimber.getSettings().cutLeaves() && leaves.getType() == leavesType;
    }

    public void breakTree() {
        int maxBlocks = WolfyUtilities.hasPermission(player, "wolfytimber.limit_bypass") ? 83072 : WolfyTimber.getSettings().maxBlocks();
        this.task = new BukkitRunnable(){
            @Override
            public void run() {
                ArrayList<Location> blocksUpdate = new ArrayList<>();
                ArrayList<Location> blocksNext = new ArrayList<>();
                ArrayList<Location> blocks = new ArrayList<>();
                blocks.add(startBlock.getLocation());
                blocksNext.add(startBlock.getLocation());
                int totalBlocks = 0;
                int roundFoundLogs;
                do {
                    roundFoundLogs = 0;
                    for (Location loc : blocksNext) {
                        for (int x = -1; x < 2; x++) {
                            for (int y = 0; y < 2; y++) {
                                for (int z = -1; z < 2; z++) {
                                    Block next = loc.clone().add(x, y, z).getBlock();
                                    if (!blocksUpdate.contains(next.getLocation())) {
                                        if (isWoodCorrect(next)) {
                                            blocksUpdate.add(next.getLocation());
                                            roundFoundLogs++;
                                        } else if (areLeavesCorrect(next)) {
                                            blocks.add(next.getLocation());
                                        }
                                    }
                                }
                            }
                        }
                    }
                    blocksNext.clear();
                    if (!blocksUpdate.isEmpty()) {
                        blocksNext.addAll(blocksUpdate);
                        blocks.addAll(blocksUpdate);
                        blocksUpdate.clear();
                    }
                    for (Location loc : blocks) {
                        if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
                            Bukkit.getScheduler().runTask(WolfyTimber.getInstance(), () -> loc.getBlock().breakNaturally());
                        } else {
                            Bukkit.getScheduler().runTask(WolfyTimber.getInstance(), () ->loc.getBlock().setType(Material.AIR));
                        }
                    }
                    if(ItemUtils.hasCustomDurability(tool)){
                        ItemUtils.setDamage(tool, ItemUtils.getDamage(tool)+blocks.size());
                    }
                    blocks.clear();
                    totalBlocks += roundFoundLogs;
                } while (roundFoundLogs > 0 && totalBlocks < maxBlocks);
                task.cancel();
            }
        }.runTaskAsynchronously(WolfyTimber.getInstance());
    }

    public boolean isRunning(){
        return !task.isCancelled();
    }
}
