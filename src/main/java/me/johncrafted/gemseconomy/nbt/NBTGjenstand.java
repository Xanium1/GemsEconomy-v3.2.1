/*
 * Copyright JohnCrafted (c) 2017. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of JohnCrafted. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.johncrafted.gemseconomy.nbt;

import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 08.05.2017.
 **/
public class NBTGjenstand {

    private ItemStack bukkititem;

    public NBTGjenstand(ItemStack Item) {
        bukkititem = Item.clone();
    }

    public ItemStack getItem() {
        return bukkititem;
    }

    public void setString(String Key, String Text) {
        bukkititem = NBTRefleksjon.setString(bukkititem, Key, Text);
    }

    public String getString(String Key) {
        return NBTRefleksjon.getString(bukkititem, Key);
    }

    public void setInteger(String key, Integer Int) {
        bukkititem = NBTRefleksjon.setInt(bukkititem, key, Int);
    }

    public Integer getInteger(String key) {
        return NBTRefleksjon.getInt(bukkititem, key);
    }

    public void setDouble(String key, Double d) {
        bukkititem = NBTRefleksjon.setDouble(bukkititem, key, d);
    }

    public Double getDouble(String key) {
        return NBTRefleksjon.getDouble(bukkititem, key);
    }

    public void setBoolean(String key, Boolean b) {
        bukkititem = NBTRefleksjon.setBoolean(bukkititem, key, b);
    }

    public Boolean getBoolean(String key) {
        return NBTRefleksjon.getBoolean(bukkititem, key);
    }

    public Boolean hasKey(String key) {
        return NBTRefleksjon.hasKey(bukkititem, key);
    }
}
