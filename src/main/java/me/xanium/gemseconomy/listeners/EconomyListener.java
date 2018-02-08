package me.xanium.gemseconomy.listeners;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.backend.Hikari;
import me.xanium.gemseconomy.backend.UserConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by John on 10.06.2017.
 **/
public class EconomyListener implements Listener {

    private GemsCore plugin = GemsCore.getInstance();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (GemsCore.isHikari()) {
            Hikari.createPlayerIfNotExists(player);
        } else {
            UserConfig userConfig = UserConfig.getInstance();
            if (plugin.getData().get(player.getUniqueId().toString()) == null) {
                plugin.getData().set(player.getUniqueId().toString(), player.getName());
                userConfig.saveConfig(plugin, "loggedPlayers");
                userConfig.getConfig(player.getUniqueId()).set("Name", player.getName());
                userConfig.getConfig(player.getUniqueId()).set("UniqueID", player.getUniqueId().toString());
                userConfig.getConfig(player.getUniqueId()).set("Balance", GemsCore.getInstance().getConfig().getLong("Settings.startingbal"));
                userConfig.saveUser(player.getUniqueId());
            } else {
                userConfig.getConfig(player.getUniqueId()).set("Name", player.getName());
                if (userConfig.getConfig(player.getUniqueId()).getDouble("Balance") < 0) {
                    userConfig.getConfig(player.getUniqueId()).set("Balance", 0);
                }
                userConfig.saveUser(player.getUniqueId());
            }
        }
        checkCache(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (GemsCore.isHikari()) {
            GemsCore.getAccounts().put(player.getUniqueId(), Hikari.getBalance(player.getUniqueId()));
        } else {
            UserConfig userConfig = UserConfig.getInstance();
            GemsCore.getAccounts().put(player.getUniqueId(), userConfig.getConfig(player.getUniqueId()).getDouble("Balance"));
        }

    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!GemsCore.isHikari()) {
            UserConfig uc = UserConfig.getInstance();
            uc.getConfig(player.getUniqueId()).set("Balance", GemsCore.getAccounts().get(player.getUniqueId()));
            uc.saveUser(player.getUniqueId());
        }
        GemsCore.getAccounts().remove(player.getUniqueId());
    }

    private void checkCache(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!GemsCore.getAccounts().containsKey(player.getUniqueId())) {
                    if (GemsCore.isHikari()) {
                        double balance = Hikari.getBalance(player.getUniqueId());
                        GemsCore.getAccounts().put(player.getUniqueId(), balance);
                    } else {
                        UserConfig userConfig = UserConfig.getInstance();
                        GemsCore.getAccounts().put(player.getUniqueId(), userConfig.getConfig(player.getUniqueId()).getDouble("Balance"));
                    }
                }
            }
        }.runTaskLater(GemsCore.getInstance(), 20L);
    }
}
