package me.wolfyscript.timber.commands;

import me.wolfyscript.timber.WolfyTimber;
import me.wolfyscript.utilities.api.utils.ItemUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimberCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("get")) {
                    if (ItemUtils.hasCCSettings(player.getInventory().getItemInMainHand())) {
                        WolfyTimber.getApi().sendPlayerMessage(player, "Damage: " + ItemUtils.getDamage(player.getInventory().getItemInMainHand()));
                        WolfyTimber.getApi().sendPlayerMessage(player, "Durability: " + ItemUtils.getCustomDurability(player.getInventory().getItemInMainHand()));
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    ItemUtils.setCustomDurability(player.getInventory().getItemInMainHand(), 3000);
                }
            }
        }
        return false;
    }
}
