package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.api.GemsAPI;
import me.xanium.gemseconomy.utils.FormatUtil;
import me.xanium.gemseconomy.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
            if(args.length == 1) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                if(player != null) {
                    if (!player.isOnline()) {
                        if (GemsAPI.getBalance(player.getUniqueId()) != -1) {
                            sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", player.getName()).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(player.getUniqueId()))));
                            return true;
                        }
                    }
                    if (GemsCore.getAccounts().containsKey(player.getUniqueId())) {
                        sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", player.getName()).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(player.getUniqueId()))));
                        return true;
                    }
                }
                sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                return true;
            }
            return true;
        }
        Player p = (Player) sender;
        if(!p.hasPermission("gemseconomy.command.balance")){
            p.sendMessage(Messages.getPrefix() + Messages.getNoPerms());
            return true;
        }
        if(args.length == 0){
            p.sendMessage(Messages.getPrefix() + Messages.getBalance().replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(p.getUniqueId()))));
            return true;
        } else{
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if(player != null) {
                if (!player.isOnline()) {
                    if (GemsAPI.getBalance(player.getUniqueId()) != -1) {
                        sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", player.getName()).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(player.getUniqueId()))));
                        return true;
                    }
                }
                if (GemsCore.getAccounts().containsKey(player.getUniqueId())) {
                    sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", player.getName()).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(player.getUniqueId()))));
                    return true;
                }
            }
            sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
            return true;
        }
    }
}
