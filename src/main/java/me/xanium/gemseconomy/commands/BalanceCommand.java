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

import java.util.UUID;

/**
 * Created by John on 11.09.2016.
 * Recoded on 11.06.2017.
 **/
public class BalanceCommand implements CommandExecutor {

    private final GemsCore plugin = GemsCore.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            if (!(sender instanceof Player)) {
                if (args.length == 0) {
                    sender.sendMessage(Messages.getBalanceConsole());
                    return;
                }
                if (args.length == 1) {
                    UUID id = plugin.searchForUser(args[0]);
                    if (id != null) {
                        if (plugin.getServer().getPlayer(id) == null) {
                            if (GemsAPI.getBalance(id) != -1) {
                                sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", args[0]).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(id))));
                                return;
                            }
                        }
                        if (GemsCore.getAccounts().containsKey(id)) {
                            sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", args[0]).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(id))));
                            return;
                        }
                    }
                    sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
                    return;
                }
                return;
            }
            Player p = (Player) sender;
            if (!p.hasPermission("gemseconomy.command.balance")) {
                p.sendMessage(Messages.getPrefix() + Messages.getNoPerms());
                return;
            }
            if (args.length == 0) {
                p.sendMessage(Messages.getPrefix() + Messages.getBalance().replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(p.getUniqueId()))));
                return;
            } else {
                UUID id = plugin.searchForUser(args[0]);
                if (id != null) {
                    if (plugin.getServer().getPlayer(id) == null) {
                        if (GemsAPI.getBalance(id) != -1) {
                            sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", args[1]).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(id))));
                            return;
                        }
                    }
                    if (GemsCore.getAccounts().containsKey(id)) {
                        sender.sendMessage(Messages.getPrefix() + Messages.getBalanceOther().replace("{player}", args[1]).replace("{gems}", FormatUtil.formatNumber(GemsAPI.getBalance(id))));
                        return;
                    }
                }
                sender.sendMessage(Messages.getPrefix() + Messages.getPlayerDoesNotExist());
            }
        });
        return true;
    }
}
