package me.johncrafted.gemseconomy;

import me.johncrafted.gemseconomy.backend.ConfigWriter;
import me.johncrafted.gemseconomy.backend.Hikari;
import me.johncrafted.gemseconomy.backend.UserConfig;
import me.johncrafted.gemseconomy.commands.BalanceCommand;
import me.johncrafted.gemseconomy.commands.ChequeCommand;
import me.johncrafted.gemseconomy.commands.EconomyCommand;
import me.johncrafted.gemseconomy.commands.PayCommand;
import me.johncrafted.gemseconomy.listeners.ChequeListener;
import me.johncrafted.gemseconomy.listeners.EconomyListener;
import me.johncrafted.gemseconomy.nbt.NMSVersjon;
import me.johncrafted.gemseconomy.utils.Cheque;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
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
    private static Map<UUID, Long> accounts = new HashMap<>();
    private static ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
    private NMSVersjon nmsVersion;
    private ConfigWriter defcfg;
    public static boolean mysql = false;

    @Override
    public void onEnable(){
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Enabling..");
        instance = this;
        nmsVersion = new NMSVersjon();

        defcfg = new ConfigWriter(this);

        registerListeners();
        registerCommands();
        loadStorage();
        loadAccounts();
        if(getConfig().getBoolean("cheque.enable")) {
            Cheque.setChequeBase();
        }
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
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("eco").setExecutor(new EconomyCommand());
        getCommand("pay").setExecutor(new PayCommand());
        if(getConfig().getBoolean("cheque.enable")) {
            getCommand("cheque").setExecutor(new ChequeCommand());
        }
        consoleCommandSender.sendMessage("§a[GemsEconomy] §7Loaded commands.");
    }

    private void loadAccounts(){
        for (Player players : Bukkit.getOnlinePlayers()){
            if(GemsCore.isHikari()){
                GemsCore.getAccounts().put(players.getUniqueId(), Hikari.getSavedBalance(players));
            }else{
                UserConfig userConfig = UserConfig.getInstance();
                GemsCore.getAccounts().put(players.getUniqueId(), userConfig.getConfig(players).getLong("Balance"));
            }
        }
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

    public static Map<UUID, Long> getAccounts() {
        return accounts;
    }

    public static ConsoleCommandSender getConsole() {
        return consoleCommandSender;
    }

    public NMSVersjon getNmsVersion() {
        return nmsVersion;
    }
}
