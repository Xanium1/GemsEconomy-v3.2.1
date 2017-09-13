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
                    if (UtilNumber.validateInput(player, args[1])) {
                        long amount = Long.valueOf(args[1]);
                        if (GemsCore.getAccounts().get(player.getUniqueId()) >= amount) {
                            Hikari.updateBalance(EcoAction.WITHDRAW, player, amount);
                            Hikari.updateBalance(EcoAction.DEPOSIT, target, amount);
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
