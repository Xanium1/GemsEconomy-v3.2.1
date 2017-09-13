package me.johncrafted.gemseconomy.commands;

import me.johncrafted.gemseconomy.GemsCore;
import me.johncrafted.gemseconomy.backend.Hikari;
import me.johncrafted.gemseconomy.api.EcoAction;
import me.johncrafted.gemseconomy.utils.Messages;
import me.johncrafted.gemseconomy.utils.UtilNumber;
import org.bukkit.Bukkit;
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
            sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
            return true;
        }
        if(args.length == 0){
            Messages.getManageHelp(sender);
            return true;
        }
        if(args.length == 3){
            if(args[0].equalsIgnoreCase("add")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    if(UtilNumber.validateInput(sender, args[2])){
                        long amount = Long.valueOf(args[2]);
                        Hikari.updateBalance(EcoAction.DEPOSIT, target, amount);
                        sender.sendMessage(Messages.getPrefix() + Messages.getAddMessage().replace("{player}", target.getName()).replace("{amount}", args[2]));
                        return true;
                    }
                }else{
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                }
            }
            else if(args[0].equalsIgnoreCase("take")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    if(UtilNumber.validateInput(sender, args[2])){
                        long amount = Long.valueOf(args[2]);
                        if(GemsCore.getAccounts().get(target.getUniqueId()) >= amount) {
                            Hikari.updateBalance(EcoAction.WITHDRAW, target, amount);
                            sender.sendMessage(Messages.getPrefix() + Messages.getTakeMessage().replace("{player}", target.getName()).replace("{amount}", args[2]));
                        }else{
                            sender.sendMessage(Messages.getPrefix() + Messages.getNegativeValue());
                        }
                    }
                }else{
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                }
            }
            else if(args[0].equalsIgnoreCase("set")){
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null){
                    if(UtilNumber.validateInput(sender, args[2])){
                        long amount = Long.valueOf(args[2].replace("-", ""));
                        Hikari.updateBalance(EcoAction.SET, target, amount);
                        sender.sendMessage(Messages.getPrefix() + Messages.getSetMessage().replace("{player}", target.getName()).replace("{amount}", args[2]));
                    }
                }else{
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                }
            }
        }
        return true;
    }
}
