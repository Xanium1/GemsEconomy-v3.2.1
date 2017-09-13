package me.johncrafted.gemseconomy.listeners;

import me.johncrafted.gemseconomy.GemsCore;
import me.johncrafted.gemseconomy.api.EcoAction;
import me.johncrafted.gemseconomy.api.GemsAPI;
import me.johncrafted.gemseconomy.nbt.NBTGjenstand;
import me.johncrafted.gemseconomy.utils.Cheque;
import me.johncrafted.gemseconomy.utils.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by John on 16.07.2017.
 **/
public class ChequeListener implements Listener {

    @EventHandler
    public void onRedeem(PlayerInteractEvent event){
        Player player = event.getPlayer();

        if(player.hasPermission("gemseconomy.fastredeem")) {
            if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType().equals(Material.valueOf(GemsCore.getInstance().getConfig().getString("cheque.material")))) {
                NBTGjenstand item = new NBTGjenstand(player.getInventory().getItemInMainHand());
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
