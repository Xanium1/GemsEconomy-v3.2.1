package me.xanium.gemseconomy.commands;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.api.EcoAction;
import me.xanium.gemseconomy.api.GemsAPI;
import me.xanium.gemseconomy.backend.Hikari;
import me.xanium.gemseconomy.utils.FormatUtil;
import me.xanium.gemseconomy.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Xanium on 11.06.2017.
 **/
public class EconomyCommand implements CommandExecutor {

    private GemsCore plugin = GemsCore.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {

            if (!sender.hasPermission("gemseconomy.command.economy")) {
                sender.sendMessage(Messages.getPrefix() + Messages.getNoPerms());
                return;
            }
            if (args.length == 0) {
                Messages.getManageHelp(sender);
                return;
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("add")) {
                    UUID id = plugin.searchForUser(args[1]);
                    if (id != null) {
                        if (FormatUtil.validateInput(sender, args[2])) {
                            double amount = Double.valueOf(args[2]);
                            if (plugin.getServer().getPlayer(id) != null) {
                                Player target = plugin.getServer().getPlayer(id);
                                Hikari.updateBalance(EcoAction.DEPOSIT, target.getUniqueId(), amount, false);
                                sender.sendMessage(Messages.getPrefix() + Messages.getAddMessage().replace("{player}", target.getName()).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                                return;
                            } else {
                                Hikari.updateBalance(EcoAction.DEPOSIT, id, amount, true);
                                sender.sendMessage(Messages.getPrefix() + Messages.getAddMessage().replace("{player}", args[1]).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                                return;
                            }
                        }
                    } else {
                        sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                    }
                } else if (args[0].equalsIgnoreCase("take")) {
                    UUID id = plugin.searchForUser(args[1]);
                    if (id != null) {
                        if (FormatUtil.validateInput(sender, args[2])) {
                            double amount = Double.valueOf(args[2]);
                            if (plugin.getServer().getPlayer(id) != null) {
                                Player target = plugin.getServer().getPlayer(id);
                                if (GemsCore.getAccounts().get(id) >= amount) {
                                    Hikari.updateBalance(EcoAction.WITHDRAW, id, amount, false);
                                    sender.sendMessage(Messages.getPrefix() + Messages.getTakeMessage().replace("{player}", target.getName()).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                                } else {
                                    sender.sendMessage(Messages.getPrefix() + Messages.getNegativeValue());
                                }
                            } else {
                                if (GemsAPI.getBalance(id) != -1 && GemsAPI.getBalance(id) >= amount) {
                                    Hikari.updateBalance(EcoAction.WITHDRAW, id, amount, true);
                                    sender.sendMessage(Messages.getPrefix() + Messages.getTakeMessage().replace("{player}", args[1]).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                                    return;
                                } else {
                                    sender.sendMessage(Messages.getPrefix() + Messages.getNegativeValue());
                                }
                            }
                        }
                    } else {
                        sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                    }
                } else if (args[0].equalsIgnoreCase("set")) {
                    UUID id = plugin.searchForUser(args[1]);
                    if (id != null) {
                        if (FormatUtil.validateInput(sender, args[2])) {
                            double amount = Double.valueOf(args[2]);
                            Hikari.updateBalance(EcoAction.SET, id, amount, false);
                            sender.sendMessage(Messages.getPrefix() + Messages.getSetMessage().replace("{player}", args[1]).replace("{amount}", FormatUtil.formatNumber(Double.valueOf(args[2]))));
                        }
                    } else {
                        sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                    }
                }
            }
        });
        return true;
    }
}
