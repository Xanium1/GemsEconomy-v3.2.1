/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.backend;

import com.zaxxer.hikari.HikariDataSource;
import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.api.EcoAction;
import me.xanium.gemseconomy.api.GemsAPI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by John on 28.03.2017.
 **/
public class Hikari {

    private static HikariDataSource hikari;
    private GemsCore plugin = GemsCore.getInstance();

    public static HikariDataSource getHikari() {
        return hikari;
    }

    public void connectToDatabase(){
        String address = plugin.getConfig().getString("mysql.host");
        int port = plugin.getConfig().getInt("mysql.port");
        String name = plugin.getConfig().getString("mysql.database");
        String username = plugin.getConfig().getString("mysql.username");
        String password = plugin.getConfig().getString("mysql.password");

        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", name);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

        try (Connection connection = hikari.getConnection()){
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `accounts` (`uuid` VARCHAR(255), UNIQUE KEY idx(uuid), `name` VARCHAR(255), `balance` DOUBLE)").executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        GemsCore.mysql = true;
    }

    public static void createPlayerIfNotExists(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                PreparedStatement p;

                String getPlayer = "SELECT * FROM `accounts` WHERE `uuid`=?";
                String updatePlayer = "UPDATE `accounts` SET `name`=? WHERE `uuid`=?";
                String createPlayer = "INSERT INTO `accounts` (`uuid`, `name`, `balance`) VALUES (?,?,?)";

                try (Connection connection = getHikari().getConnection()){
                    p = connection.prepareStatement(getPlayer);
                    p.setString(1, player.getUniqueId().toString());

                    ResultSet rs = p.executeQuery();
                    if(rs.next()){
                        if(!rs.getString("name").equalsIgnoreCase(player.getName())){
                            p = connection.prepareStatement(updatePlayer);
                            p.setString(1, player.getName());
                            p.setString(2, player.getUniqueId().toString());
                            p.executeUpdate();
                        }
                    }else{
                        p = connection.prepareStatement(createPlayer);
                        p.setString(1, player.getUniqueId().toString());
                        p.setString(2, player.getName());
                        p.setDouble(3, GemsCore.getInstance().getConfig().getLong("Settings.startingbal"));
                        p.executeUpdate();
                    }
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(GemsCore.getInstance());
    }

    public static void updateBalance(EcoAction action, UUID uuid, double amount, boolean offline){
        new BukkitRunnable(){
            @Override
            public void run() {
                double current = GemsAPI.getBalance(uuid);
                PreparedStatement p;
                String updateString = "UPDATE `accounts` SET `balance`=? WHERE `uuid`=?";
                UserConfig userConfig = UserConfig.getInstance();

                if(action == EcoAction.DEPOSIT){
                    // Add cash
                    if(GemsCore.isHikari()) {
                        try (Connection connection = getHikari().getConnection()) {
                            p = connection.prepareStatement(updateString);
                            p.setDouble(1, current + amount);
                            p.setString(2, uuid.toString());
                            p.executeUpdate();
                            if(!offline) {
                                GemsCore.getAccounts().put(uuid, current + amount);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        userConfig.getConfig(uuid).set("Balance", current + amount);
                        if(!offline) {
                            GemsCore.getAccounts().put(uuid, current + amount);
                        }
                        userConfig.saveUser(uuid);
                    }
                }
                else if(action == EcoAction.WITHDRAW){
                    // Remove cash
                    if(GemsCore.isHikari()) {
                        try (Connection connection = getHikari().getConnection()) {
                            p = connection.prepareStatement(updateString);
                            p.setDouble(1, current - amount);
                            p.setString(2, uuid.toString());
                            p.executeUpdate();
                            if(!offline) {
                                GemsCore.getAccounts().put(uuid, current - amount);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        userConfig.getConfig(uuid).set("Balance", current - amount);
                        if(!offline) {
                            GemsCore.getAccounts().put(uuid, current - amount);
                        }
                        userConfig.saveUser(uuid);
                    }
                }
                else if(action == EcoAction.SET){
                    if(GemsCore.isHikari()) {
                        try (Connection connection = getHikari().getConnection()) {
                            p = connection.prepareStatement(updateString);
                            p.setDouble(1, amount);
                            p.setString(2, uuid.toString());
                            p.executeUpdate();
                            if(!offline) {
                                GemsCore.getAccounts().put(uuid, amount);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        userConfig.getConfig(uuid).set("Balance", amount);
                        if(!offline) {
                            GemsCore.getAccounts().put(uuid, amount);
                        }
                        userConfig.saveUser(uuid);
                    }
                }
            }
        }.runTaskAsynchronously(GemsCore.getInstance());
    }

    public static double getBalance(UUID uuid){
        PreparedStatement p;
        String getDataString = "SELECT * FROM `accounts` WHERE `uuid`=?";

        try (Connection connection = getHikari().getConnection()){
            p = connection.prepareStatement(getDataString);
            p.setString(1, uuid.toString());
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                return rs.getDouble("balance");
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

        return -1;
    }
}
