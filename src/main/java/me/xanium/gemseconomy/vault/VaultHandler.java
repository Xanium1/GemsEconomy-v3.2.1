/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.vault;

import me.xanium.gemseconomy.GemsCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class VaultHandler {

    private GemsEconomy economy = null;
    private GemsCore plugin;

    public VaultHandler(GemsCore plugin){
        this.plugin = plugin;
    }

    public void hook() {
        try {
            if (this.economy == null) {
                this.economy = new GemsEconomy();
            }

            ServicesManager sm = Bukkit.getServicesManager();
            sm.register(Economy.class, this.economy, plugin, ServicePriority.High);
        } catch (Exception e) {
            if(plugin.isDebug()) {
                e.printStackTrace();
            }else{
                plugin.getConsole().sendMessage("§a[GemsEconomy] §7An exception were triggered. Enable debug to view stack trace.");
            }
        }
    }

    public void unhook() {
        ServicesManager sm = Bukkit.getServicesManager();
        if(this.economy != null){
            sm.unregister(Economy.class, this.economy);
            this.economy = null;
        }
    }

}
