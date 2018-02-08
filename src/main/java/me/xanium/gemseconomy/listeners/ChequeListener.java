package me.xanium.gemseconomy.listeners;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.api.EcoAction;
import me.xanium.gemseconomy.api.GemsAPI;
import me.xanium.gemseconomy.nbt.NBTItem;
import me.xanium.gemseconomy.utils.Cheque;
import me.xanium.gemseconomy.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by John on 16.07.2017.
 **/
public class ChequeListener implements Listener {

    @EventHandler
    public void onRedeem(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(player.hasPermission("gemseconomy.fastredeem")) {
            if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType().equals(Material.valueOf(GemsCore.getInstance().getConfig().getString("cheque.material")))) {
                NBTItem item = new NBTItem(player.getInventory().getItemInMainHand());
                if (item.getItem().getItemMeta().hasDisplayName() && item.getItem().getItemMeta().hasLore() && item.hasKey("value")) {
                    if (Cheque.isAValidCheque(item)) {
                        if (item.getString("value") != null) {
                            long value = Long.parseLong(item.getString("value"));

                            if (item.getItem().getAmount() > 1) {
                                item.getItem().setAmount(item.getItem().getAmount() - 1);
                                GemsAPI.editBalance(EcoAction.DEPOSIT, player, value);
                                player.sendMessage(Messages.getPrefix() + Messages.getChequeRedeemed());
                                return;
                            }
                            player.getInventory().remove(item.getItem());
                            GemsAPI.editBalance(EcoAction.DEPOSIT, player, value);
                            player.sendMessage(Messages.getPrefix() + Messages.getChequeRedeemed());
                        }
                    }
                }
            }
        }
    }
}
