package me.johncrafted.gemseconomy.listeners;

import me.johncrafted.gemseconomy.GemsCore;
import me.johncrafted.gemseconomy.backend.Hikari;
import me.johncrafted.gemseconomy.backend.UserConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

/**
 * Created by John on 10.06.2017.
 **/
public class EconomyListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();
        if(GemsCore.isHikari()) {
            Hikari.createPlayerIfNotExists(player);
        }
        else{
            UserConfig userConfig = UserConfig.getInstance();
            userConfig.getConfig(player).set("Name", player.getName());
            userConfig.getConfig(player).set("UniqueID", player.getUniqueId().toString());
            if(userConfig.getConfig(player).getLong("Balance") <= 0) {
                userConfig.getConfig(player).set("Balance", 0);
            }
            userConfig.saveUser(player);
        }
        checkCache(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (GemsCore.isHikari()) {
            GemsCore.getAccounts().put(player.getUniqueId(), Hikari.getSavedBalance(player));
        } else {
            UserConfig userConfig = UserConfig.getInstance();
            GemsCore.getAccounts().put(player.getUniqueId(), userConfig.getConfig(player).getLong("Balance"));
        }

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(!GemsCore.isHikari()){
            UserConfig uc = UserConfig.getInstance();
            uc.getConfig(player).set("Balance", GemsCore.getAccounts().get(player.getUniqueId()));
            uc.saveUser(player);
        }
        GemsCore.getAccounts().remove(player.getUniqueId());
    }

    private void checkCache(Player player){
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!GemsCore.getAccounts().containsKey(player.getUniqueId())){
                    if (GemsCore.isHikari()) {
                        long balance = 0;
                        try{
                            balance = Hikari.getSavedBalance(player);
                        }catch(Exception ex){ }
                        GemsCore.getAccounts().put(player.getUniqueId(), balance);
                    } else {
                        UserConfig userConfig = UserConfig.getInstance();
                        GemsCore.getAccounts().put(player.getUniqueId(), userConfig.getConfig(player).getLong("Balance"));
                    }
                }
            }
        }.runTaskLater(GemsCore.getInstance(), 20L);
    }
}
