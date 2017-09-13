package me.johncrafted.gemseconomy.utils;

import me.johncrafted.gemseconomy.GemsCore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by John on 11.06.2017.
 **/
public class Messages {

    private static GemsCore plugin = GemsCore.getInstance();
    private static FileConfiguration cfg = plugin.getConfig();

    public static String getPrefix(){
        return colorize(cfg.getString("Messages.prefix"));
    }
    public static String getNoPerms(){
        return colorize(cfg.getString("Messages.nopermission"));
    }
    public static String getNoConsole(){
        return colorize(cfg.getString("Messages.noconsole"));
    }
    public static String getPaidMessage(){
        return colorize(cfg.getString("Messages.paid"));
    }
    public static String getPayerMessage(){
        return colorize(cfg.getString("Messages.payer"));
    }
    public static String getPayHelp(){
        return colorize(cfg.getString("Messages.payhelp"));
    }
    public static String getAddMessage(){
        return colorize(cfg.getString("Messages.add"));
    }
    public static String getTakeMessage(){
        return colorize(cfg.getString("Messages.take"));
    }
    public static String getSetMessage(){
        return colorize(cfg.getString("Messages.set"));
    }
    public static String getPlayerDoesNotExist(){
        return colorize(cfg.getString("Messages.player_is_null"));
    }
    public static String getUnsufficientfunds(){
        return colorize(cfg.getString("Messages.unsufficientfunds"));
    }
    public static String getPayYourself(){
        return colorize(cfg.getString("Messages.pay_yourself"));
    }
    public static void getManageHelp(CommandSender sender){
        for(String s : cfg.getStringList("Messages.managehelp")){
            sender.sendMessage(colorize(s));
        }
    }
    public static String getBalance(){
        return colorize(cfg.getString("Messages.balance"));
    }
    public static String getBalanceOther(){
        return colorize(cfg.getString("Messages.balanceother"));
    }
    public static String getBalanceConsole(){
        return colorize(cfg.getString("Messages.balanceconsole"));
    }
    public static String getNegativeValue(){
        return colorize(cfg.getString("Messages.negativevalue"));
    }
    public static String getUnvalidAmount(){
        return colorize(cfg.getString("Messages.unvalidamount"));
    }
    public static void getChequeHelp(CommandSender sender){
        for(String s : cfg.getStringList("Messages.chequehelp")){
            sender.sendMessage(colorize(s));
        }
    }
    public static String getChequeSucess(){
        return colorize(cfg.getString("Messages.cheque_success"));
    }
    public static String getChequeRedeemed(){
        return colorize(cfg.getString("Messages.cheque_redeemed"));
    }
    public static String getChequeInvalid(){
        return colorize(cfg.getString("Messages.cheque_invalid"));
    }

    private static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
