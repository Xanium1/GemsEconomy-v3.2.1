/*
 * Copyright JohnCrafted (c) 2016. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of JohnCrafted. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.utils;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.nbt.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 16.07.2017.
 **/
public class Cheque {

    private static ItemStack chequeBaseItem;
    private static String nbttag = "value";
    //private static final Pattern MONEY_PATTERN = Pattern.compile("((([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.?\\d?\\d?)?)");

    public static void setChequeBase(){
        ItemStack item = new ItemStack(Material.valueOf(GemsCore.getInstance().getConfig().getString("cheque.material")), 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(FormatUtil.colorize(GemsCore.getInstance().getConfig().getString("cheque.name")));
        meta.setLore(FormatUtil.colorize(GemsCore.getInstance().getConfig().getStringList("cheque.lore")));
        item.setItemMeta(meta);
        chequeBaseItem = item;
    }

    public ItemStack writeCheque(String creatorName, double amount) {
        if (creatorName.equals("CONSOLE")) {
            creatorName = FormatUtil.colorize(GemsCore.getInstance().getConfig().getString("cheque.console_name"));
        }
        List<String> formatLore = new ArrayList<>();

        for (String baseLore2 : chequeBaseItem.getItemMeta().getLore()) {
            formatLore.add(baseLore2.replace("{gems}", String.valueOf(amount)).replace("{player}", creatorName));
        }
        ItemStack ret = chequeBaseItem.clone();
        NBTItem nbt = new NBTItem(ret);
        ItemMeta meta = nbt.getItem().getItemMeta();
        meta.setLore(formatLore);
        nbt.getItem().setItemMeta(meta);
        nbt.setString(nbttag, String.valueOf(amount));

        return nbt.getItem();
    }

    public static boolean isAValidCheque(NBTItem itemstack) {
        if (itemstack.getItem().getType() == chequeBaseItem.getType() && itemstack.getString(nbttag) != null && itemstack.getItem().getItemMeta().hasLore()) {
            String display = chequeBaseItem.getItemMeta().getDisplayName();
            if(itemstack.getItem().getItemMeta().getDisplayName().equals(display) && itemstack.getItem().getItemMeta().hasLore()){
                return (itemstack.getItem().getItemMeta().getDisplayName().equals(display) && itemstack.getItem().getItemMeta().getLore().size() == chequeBaseItem.getItemMeta().getLore().size());
            }
            return false;
        }
        return false;
    }

    public static long getChequeValue(NBTItem itemstack) {
        if (itemstack.getString(nbttag) != null) {
            return Long.parseLong(itemstack.getString(nbttag));
        }
        return 0;
    }
}
