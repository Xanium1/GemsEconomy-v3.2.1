package me.johncrafted.gemseconomy.backend;

import me.johncrafted.gemseconomy.GemsCore;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

/**
 * Created by John on 10.06.2017.
 **/
public class ConfigWriter {

    private GemsCore plugin;

    public ConfigWriter(GemsCore plugin){
        this.plugin = plugin;
        loadDefaultConfig();
    }

    private void loadDefaultConfig() {

        FileConfiguration config = plugin.getConfig();

        config.options().header(plugin.getDescription().getName() +
                "\nVersion: " + plugin.getDescription().getVersion() +
                "\nDeveloper(s): " + plugin.getDescription().getAuthors() +
                "\nMain configuration file. \n\nCopyrighted under (All Rights Reserved)!" +
                "\n\nAvailable storage options: yml, mysql" +
                "\n\nYou can customize the messages as you want. If you have an mysql database,\nyou'll need to write the login credentials down under." +
                "\n\nWARNING: Do not reload the server if you are using yml/flatfile storage!!\nIf you really want to reload the server, you will have to kick all the players first!!");

        config.addDefault("storage", "yml");

        config.addDefault("mysql.database", "minecraft");
        config.addDefault("mysql.host", "localhost");
        config.addDefault("mysql.port", 3306);
        config.addDefault("mysql.username", "root");
        config.addDefault("mysql.password", "password");

        config.addDefault("Settings.startingbal", 100);

        config.addDefault("cheque.enable", true);
        config.addDefault("cheque.material", Material.PAPER.toString());
        config.addDefault("cheque.name", "&aGems Cheque");
        config.addDefault("cheque.lore", Arrays.asList("&7Value: {gems} gems.", "&7&oWritten by {player}"));
        config.addDefault("cheque.console_name", "Console");

        config.addDefault("Messages.prefix", "&a&lGemsEconomy> ");
        config.addDefault("Messages.nopermission", "&7You don't have permission to do this.");
        config.addDefault("Messages.noconsole", "&7Console cannot do this.");
        config.addDefault("Messages.balanceconsole", "&2&l>> &a/bal <user> &8- &7Shows user balance.");
        config.addDefault("Messages.unsufficientfunds", "&7You don't have enough gems.");
        config.addDefault("Messages.negativevalue", "&7You can't remove that much.");
        config.addDefault("Messages.unvalidamount", "&7Not a valid amount.");
        config.addDefault("Messages.pay_yourself", "&7You can't pay yourself.");
        config.addDefault("Messages.player_is_null", "&7The specified player does not exist.");
        config.addDefault("Messages.managehelp", Arrays.asList(
                "&2&l>> &a/eco add <user> <amount> &8- &7&oAdd an amount of gems to a user.",
                "&2&l>> &a/eco take <user> <amount> &8- &7&oRemove an amount of gems to a user.",
                "&2&l>> &a/eco set <user> <amount> &8- &7&oSet an amount of gems to a user."));
        config.addDefault("Messages.payhelp", "&2&l>> &a/pay <user> <amount> &8- &7Pay the specified user the specified amount.");
        config.addDefault("Messages.paid", "&7You were paid &f{amount} &7from &a{player}&7.");
        config.addDefault("Messages.payer", "&7You paid &a{player} &7an amount of &f{amount}&7.");
        config.addDefault("Messages.add", "&7You gave &a{player} &f{amount}&7 gem(s).");
        config.addDefault("Messages.take", "&7You took &f{amount} &7gem(s) from &a{player}&7.");
        config.addDefault("Messages.set", "&7You set &f{player}&7's gems to &f{amount}&7 gem(s).");
        config.addDefault("Messages.balance", "&7Your balance is: &f{gems} &7gem(s).");
        config.addDefault("Messages.balanceother", "&a{player}&7's balance is: &f{gems} &7gem(s).");
        config.addDefault("Messages.chequehelp", Arrays.asList(
                "&2&l>> &a/cheque write <amount> &8- &7Write a cheque with a specified amount.",
                "&2&l>> &a/cheque redeem &8- &7&oRedeem the cheque."));
        config.addDefault("Messages.cheque_success", "&7Cheque successfully written.");
        config.addDefault("Messages.cheque_redeemed", "&7Cheque has been cashed in.");
        config.addDefault("Messages.cheque_invalid", "&7This is not a valid cheque.");

        config.options().copyDefaults(true);
        plugin.saveConfig();
        plugin.reloadConfig();
    }

}
