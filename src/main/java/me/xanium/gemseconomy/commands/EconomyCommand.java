package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.api.GemsAPI;
import me.xanium.gemseconomy.backend.Hikari;
import me.xanium.gemseconomy.api.EcoAction;
import me.xanium.gemseconomy.utils.FormatUtil;
import me.xanium.gemseconomy.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 11.06.2017.
 **/
public class EconomyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!sender.hasPermission("gemseconomy.command.economy")){
            sender.sendMessage(Messages.getPrefix() + Messages.getNoPerms());
            return true;
        }
        if(args.length == 0){
            Messages.getManageHelp(sender);
            return true;
        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("add")){
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(target != null){
                    if(FormatUtil.validateInput(sender, args[2])){
                        double amount = Double.valueOf(args[2]);
                        if(target.isOnline()) {
                            Hikari.updateBalance(EcoAction.DEPOSIT, target.getUniqueId(), amount, false);
                            sender.sendMessage(Messages.getPrefix() + Messages.getAddMessage().replace("{player}", target.getName()).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                            return true;
                        }else{
                            Hikari.updateBalance(EcoAction.DEPOSIT, target.getUniqueId(), amount, true);
                            sender.sendMessage(Messages.getPrefix() + Messages.getAddMessage().replace("{player}", target.getName()).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                            return true;
                        }
                    }
                }else{
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                }
            }
            else if(args[0].equalsIgnoreCase("take")){
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(target != null){
                    if(FormatUtil.validateInput(sender, args[2])){
                        double amount = Double.valueOf(args[2]);
                        if(target.isOnline()) {
                            if (GemsCore.getAccounts().get(target.getUniqueId()) >= amount) {
                                Hikari.updateBalance(EcoAction.WITHDRAW, target.getUniqueId(), amount, false);
                                sender.sendMessage(Messages.getPrefix() + Messages.getTakeMessage().replace("{player}", target.getName()).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                            } else {
                                sender.sendMessage(Messages.getPrefix() + Messages.getNegativeValue());
                            }
                        }else{
                            if(GemsAPI.getBalance(target.getUniqueId()) != -1 && GemsAPI.getBalance(target.getUniqueId()) >= amount){
                                Hikari.updateBalance(EcoAction.WITHDRAW, target.getUniqueId(), amount, true);
                                sender.sendMessage(Messages.getPrefix() + Messages.getTakeMessage().replace("{player}", target.getName()).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                                return true;
                            } else {
                                sender.sendMessage(Messages.getPrefix() + Messages.getNegativeValue());
                            }
                        }
                    }
                }else{
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                }
            }
            else if(args[0].equalsIgnoreCase("set")){
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if(target != null){
                    if(FormatUtil.validateInput(sender, args[2])){
                        double amount = Double.valueOf(args[2]);
                        Hikari.updateBalance(EcoAction.SET, target.getUniqueId(), amount, false);
                        sender.sendMessage(Messages.getPrefix() + Messages.getSetMessage().replace("{player}", target.getName()).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                    }
                }else{
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                }
            }
        }
        return true;
    }
}
