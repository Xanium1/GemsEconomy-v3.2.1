package me.johncrafted.gemseconomy.backend;

import com.zaxxer.hikari.HikariDataSource;
import me.johncrafted.gemseconomy.GemsCore;
import me.johncrafted.gemseconomy.api.EcoAction;
import me.johncrafted.gemseconomy.api.GemsAPI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        hikari.setMaximumPoolSize(20);
        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", address);
        hikari.addDataSourceProperty("port", port);
        hikari.addDataSourceProperty("databaseName", name);
        hikari.addDataSourceProperty("user", username);
        hikari.addDataSourceProperty("password", password);

        try (Connection connection = hikari.getConnection()){
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS `accounts` (`uuid` VARCHAR(255), UNIQUE KEY idx(uuid), `name` VARCHAR(255), `balance` LONG)").executeUpdate();
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
                        p.setLong(3, GemsCore.getInstance().getConfig().getLong("Settings.startingbal"));
                        p.executeUpdate();
                    }
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(GemsCore.getInstance());
    }

    public static void updateBalance(EcoAction action, Player player, long amount){
        new BukkitRunnable(){
            @Override
            public void run() {
                long current = GemsAPI.getBalance(player);
                PreparedStatement p;
                String updateString = "UPDATE `accounts` SET `balance`=? WHERE `uuid`=?";
                UserConfig userConfig = UserConfig.getInstance();

                if(action == EcoAction.DEPOSIT){
                    // Add cash
                    if(GemsCore.isHikari()) {
                        try (Connection connection = getHikari().getConnection()) {
                            p = connection.prepareStatement(updateString);
                            p.setLong(1, current + amount);
                            p.setString(2, player.getUniqueId().toString());
                            p.executeUpdate();
                            GemsCore.getAccounts().put(player.getUniqueId(), current + amount);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        userConfig.getConfig(player).set("Balance", current + amount);
                        GemsCore.getAccounts().put(player.getUniqueId(), current + amount);
                        userConfig.saveUser(player);
                    }
                }
                else if(action == EcoAction.WITHDRAW){
                    // Remove cash
                    if(GemsCore.isHikari()) {
                        try (Connection connection = getHikari().getConnection()) {
                            p = connection.prepareStatement(updateString);
                            p.setLong(1, current - amount);
                            p.setString(2, player.getUniqueId().toString());
                            p.executeUpdate();
                            GemsCore.getAccounts().put(player.getUniqueId(), current - amount);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        userConfig.getConfig(player).set("Balance", current - amount);
                        GemsCore.getAccounts().put(player.getUniqueId(), current - amount);
                        userConfig.saveUser(player);
                    }
                }
                else if(action == EcoAction.SET){
                    if(GemsCore.isHikari()) {
                        try (Connection connection = getHikari().getConnection()) {
                            p = connection.prepareStatement(updateString);
                            p.setLong(1, amount);
                            p.setString(2, player.getUniqueId().toString());
                            p.executeUpdate();
                            GemsCore.getAccounts().put(player.getUniqueId(), amount);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }else{
                        userConfig.getConfig(player).set("Balance", amount);
                        GemsCore.getAccounts().put(player.getUniqueId(), amount);
                        userConfig.saveUser(player);
                    }
                }
            }
        }.runTaskAsynchronously(GemsCore.getInstance());
    }

    public static long getSavedBalance(Player player){
        PreparedStatement p;
        String getDataString = "SELECT * FROM `accounts` WHERE `uuid`=?";

        try (Connection connection = getHikari().getConnection()){
            p = connection.prepareStatement(getDataString);
            p.setString(1, player.getUniqueId().toString());
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                return rs.getLong("balance");
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

        return -1;
    }
}
