/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.backend.Hikari;
import me.xanium.gemseconomy.api.EcoAction;
import me.xanium.gemseconomy.utils.FormatUtil;
import me.xanium.gemseconomy.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 11.09.2016.
 * Recoded on 11.06.2017.
 **/
public class PayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(Messages.getPrefix() + Messages.getNoConsole());
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("gemseconomy.command.pay")){
            player.sendMessage(Messages.getPrefix() + Messages.getNoPerms());
            return true;
        }
        if(args.length == 0){
            player.sendMessage(Messages.getPayHelp());
            return true;
        }
        if(args.length == 2){
            Player target = Bukkit.getPlayer(args[0]);
            if(target != null){
                if(!target.equals(player)) {
                    if (FormatUtil.validateInput(player, args[1])) {
                        double amount = Double.valueOf(args[1]);
                        if (GemsCore.getAccounts().get(player.getUniqueId()) >= amount) {
                            Hikari.updateBalance(EcoAction.WITHDRAW, player.getUniqueId(), amount);
                            Hikari.updateBalance(EcoAction.DEPOSIT, target.getUniqueId(), amount);
                            player.sendMessage(Messages.getPrefix() + Messages.getPayerMessage().replace("{player}", target.getName()).replace("{amount}", args[1]));
                            target.sendMessage(Messages.getPrefix() + Messages.getPaidMessage().replace("{player}", player.getName()).replace("{amount}", args[1]));
                            return true;
                        } else {
                            player.sendMessage(Messages.getPrefix() + Messages.getUnsufficientfunds());
                        }
                    }
                }else{
                    player.sendMessage(Messages.getPrefix() + Messages.getPayYourself());
                }
            }else{
                player.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
            }
        }
        return true;
    }
}
