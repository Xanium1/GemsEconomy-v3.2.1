/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy;

import me.xanium.gemseconomy.backend.ConfigWriter;
import me.xanium.gemseconomy.backend.Hikari;
import me.xanium.gemseconomy.backend.UserConfig;
import me.xanium.gemseconomy.commands.BalanceCommand;
import me.xanium.gemseconomy.commands.ChequeCommand;
import me.xanium.gemseconomy.commands.EconomyCommand;
import me.xanium.gemseconomy.commands.PayCommand;
import me.xanium.gemseconomy.listeners.ChequeListener;
import me.xanium.gemseconomy.listeners.EconomyListener;
import me.xanium.gemseconomy.nbt.NMSVersion;
import me.xanium.gemseconomy.utils.Cheque;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

/**********************************
 * Created by John on 10.06.2017. ****
 * Plugin was started on 09.04.2016. *
 *************************************/

// Plugin is created under all rights reserved license.
public class GemsCore extends JavaPlugin {

    private static GemsCore instance;
    private static Map<UUID, Double> accounts = new HashMap<>();
    private static ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
    private NMSVersion nmsVersion;
    private FileConfiguration data;
    private ConfigWriter defcfg;
    public static boolean mysql = false;
    private boolean vault = false;

    @Override
    public void onLoad(){
        defcfg = new ConfigWriter(this);
        defcfg.loadDefaultConfig();

        data = UserConfig.getInstance().getConfig(this, "loggedPlayers");
        UserConfig.getInstance().saveConfig(this, "loggedPlayers");
    }

    @Override
    public void onEnable(){
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Enabling..");
        instance = this;
        nmsVersion = new NMSVersion();

        registerListeners();
        registerCommands();
        loadStorage();
        loadAccounts();
        if(getConfig().getBoolean("cheque.enable")) {
            Cheque.setChequeBase();
        }

        setVault(vaultEnabled());
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Enabled.");
    }

    @Override
    public void onDisable(){
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Disabling..");

        if(Hikari.getHikari() !=null){
            Hikari.getHikari().close();
        }
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Disabled.");
    }

    private void loadStorage(){
        if(getConfig().getString("storage").equalsIgnoreCase("yml")){
            consoleCommandSender.sendMessage("§a[GemsEconomy] §7Storage method: yml (Flatfile).");
        }
        else if(getConfig().getString("storage").equalsIgnoreCase("mysql")){
            Hikari hikari = new Hikari();
            hikari.connectToDatabase();
            consoleCommandSender.sendMessage("§a[GemsEconomy] §7Storage method: mysql (Database).");
        }
        else{
            getLogger().log(Level.SEVERE, "No valid storage method specified in the config.yml!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new EconomyListener(), this);
        if(getConfig().getBoolean("cheque.enable")) {
            pm.registerEvents(new ChequeListener(), this);
        }
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Loaded listeners.");
    }

    private void registerCommands(){
        getCommand("gbalance").setExecutor(new BalanceCommand());
        getCommand("geco").setExecutor(new EconomyCommand());
        getCommand("gpay").setExecutor(new PayCommand());
        if(getConfig().getBoolean("cheque.enable")) {
            getCommand("gcheque").setExecutor(new ChequeCommand());
        }
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Loaded commands.");
    }

    private void loadAccounts(){
        for (Player players : Bukkit.getOnlinePlayers()){
            if(GemsCore.isHikari()){
                GemsCore.getAccounts().put(players.getUniqueId(), Hikari.getBalance(players.getUniqueId()));
            }else{
                UserConfig userConfig = UserConfig.getInstance();
                GemsCore.getAccounts().put(players.getUniqueId(), userConfig.getConfig(players.getUniqueId()).getDouble("Balance"));
            }
        }
    }

    public static boolean vaultEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public static GemsCore getInstance() {
        return instance;
    }

    public static boolean isHikari() {
        return mysql;
    }

    public ConfigWriter getConfiguration() {
        return defcfg;
    }

    public FileConfiguration getData() {
        return data;
    }

    public static Map<UUID, Double> getAccounts() {
        return accounts;
    }

    public static ConsoleCommandSender getConsole() {
        return consoleCommandSender;
    }

    public NMSVersion getNmsVersion() {
        return nmsVersion;
    }

    public boolean isVault() {
        return vault;
    }

    public void setVault(boolean vault) {
        this.vault = vault;
    }
}
