package me.johncrafted.gemseconomy.commands;

import me.johncrafted.gemseconomy.GemsCore;
import me.johncrafted.gemseconomy.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 11.09.2016.
 * Recoded on 11.06.2017.
 **/
public class BalanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            if (args.length == 0){
                sender.sendMessage(Messages.getBalanceConsole());
                return true;
            }
            if(args.length == 1){
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                    return true;
                }
                if(GemsCore.getAccounts().containsKey(target.getUniqueId())){
                    sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", target.getName()).replace("{gems}", String.valueOf(GemsCore.getAccounts().get(target.getUniqueId()))));
                    return true;
                }
            }
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("gemseconomy.command.balance")){
            player.sendMessage(Messages.getPrefix() + Messages.getNoPerms());
            return true;
        }
        if(args.length == 0){
            player.sendMessage(Messages.getPrefix() + Messages.getBalance().replace("{gems}", String.valueOf(GemsCore.getAccounts().get(player.getUniqueId()))));
            return true;
        }
        if(args.length > 0){
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null){
                player.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                return true;
            }
            if(GemsCore.getAccounts().containsKey(target.getUniqueId())){
                player.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", target.getName()).replace("{gems}", String.valueOf(GemsCore.getAccounts().get(player.getUniqueId()))));
                return true;
            }
        }
        return true;
    }
}
