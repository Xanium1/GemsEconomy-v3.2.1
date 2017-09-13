package me.johncrafted.gemseconomy.backend;

import me.johncrafted.gemseconomy.GemsCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by John on 10.06.2017.
 **/
public class UserConfig {

    private UserConfig(){}
    private static UserConfig instance = null;

    public static UserConfig getInstance() {
        if(instance == null) {
            instance = new UserConfig();
        }
        return instance;
    }

    private HashMap<JavaPlugin, HashMap<String, FileConfiguration>> configs = new HashMap<>();

    private FileConfiguration reloadConfig(JavaPlugin plugin, String id) {
        if(!configs.containsKey(plugin)) configs.put(plugin, new HashMap<>());
        File customConfigFile = new File(plugin.getDataFolder() + "/userdata/", id + ".yml");
        FileConfiguration customConfig  = YamlConfiguration.loadConfiguration(customConfigFile);
        /*InputStream defConfigStream = plugin.getResource(id + ".yml");

        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.setDefaults(defConfig);
        }*/
        configs.get(plugin).put(id, customConfig);
        return customConfig;
    }

    private FileConfiguration getConfig(JavaPlugin plugin, String id) {
        if(configs.containsKey(plugin) && configs.get(plugin).containsKey(id)) {
            return configs.get(plugin).get(id);
        }
        return reloadConfig(plugin, id);
    }

    private void saveConfig(JavaPlugin plugin, String id) {
        try {
            getConfig(plugin, id).save(new File(plugin.getDataFolder() + "/userdata/", id + ".yml"));
        }
        catch (Exception ex) {
            plugin.getLogger().severe("Could not save userdata for: " + id);
        }
    }

    public FileConfiguration getConfig(Player player){
        if(configs.containsKey(GemsCore.getInstance()) && configs.get(GemsCore.getInstance()).containsKey(player.getUniqueId().toString())) {
            return configs.get(GemsCore.getInstance()).get(player.getUniqueId().toString());
        }
        return reloadConfig(GemsCore.getInstance(), player.getUniqueId().toString());
    }

    public void saveUser(Player player){
        saveConfig(GemsCore.getInstance(), player.getUniqueId().toString());
    }

}
