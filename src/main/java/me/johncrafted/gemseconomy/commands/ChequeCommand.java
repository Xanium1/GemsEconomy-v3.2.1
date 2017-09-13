package me.johncrafted.gemseconomy.commands;

import me.johncrafted.gemseconomy.GemsCore;
import me.johncrafted.gemseconomy.api.EcoAction;
import me.johncrafted.gemseconomy.api.GemsAPI;
import me.johncrafted.gemseconomy.nbt.NBTGjenstand;
import me.johncrafted.gemseconomy.utils.Cheque;
import me.johncrafted.gemseconomy.utils.Messages;
import me.johncrafted.gemseconomy.utils.UtilNumber;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 16.07.2017.
 **/
public class ChequeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(Messages.getPrefix() + Messages.getNoConsole());
            return true;
        }
        Player player = (Player) sender;
        if(!player.hasPermission("gemseconomy.command.cheque")){
            player.sendMessage(Messages.getPrefix() + Messages.getNoPerms());
            return true;
        }
        if(args.length == 0){
            Messages.getChequeHelp(player);
            return true;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("redeem")) {
                if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType().equals(Material.valueOf(GemsCore.getInstance().getConfig().getString("cheque.material")))) {
                    NBTGjenstand item = new NBTGjenstand(player.getInventory().getItemInMainHand());
                    if (item.getItem().getItemMeta().hasDisplayName() && item.getItem().getItemMeta().hasLore() && item.hasKey("value")) {
                        if (Cheque.isAValidCheque(item)) {
                            if (item.getString("value") != null) {
                                long value = Long.parseLong(item.getString("value"));

                                if (item.getItem().getAmount() > 1) {
                                    item.getItem().setAmount(item.getItem().getAmount() - 1);
                                    GemsAPI.editBalance(EcoAction.DEPOSIT, player, value);
                                    player.sendMessage(Messages.getPrefix() + Messages.getChequeRedeemed());
                                    return true;
                                }
                                player.getInventory().remove(item.getItem());
                                GemsAPI.editBalance(EcoAction.DEPOSIT, player, value);
                                player.sendMessage(Messages.getPrefix() + Messages.getChequeRedeemed());
                                return true;
                            }
                        }
                    }
                }
            }
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("write")){
                if(UtilNumber.validateInput(sender, args[1])){
                    long amount = Long.valueOf(args[1]);
                    if(amount != 0) {
                        if (GemsAPI.getBalance(player) >= amount) {
                            Cheque c = new Cheque();
                            GemsAPI.editBalance(EcoAction.WITHDRAW, player, amount);
                            player.getInventory().addItem(c.writeCheque(player.getName(), amount));
                            player.sendMessage(Messages.getPrefix() + Messages.getChequeSucess());
                            return true;
                        } else {
                            player.sendMessage(Messages.getPrefix() + Messages.getUnsufficientfunds());
                        }
                    }else{
                        player.sendMessage(Messages.getPrefix() + Messages.getUnvalidAmount());
                    }
                }else{
                    player.sendMessage(Messages.getPrefix() + Messages.getUnvalidAmount());
                }
            }
        }
        return true;
    }
}
