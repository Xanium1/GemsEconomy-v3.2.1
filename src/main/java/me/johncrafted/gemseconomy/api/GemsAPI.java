package me.johncrafted.gemseconomy.api;

import me.johncrafted.gemseconomy.GemsCore;
import me.johncrafted.gemseconomy.backend.Hikari;
import org.bukkit.entity.Player;

/**
 * Created by John on 11.06.2017.
 **/
public class GemsAPI {

    /**
     *
     * @param action
     * @param player
     * @param amount
     * EcoAction defines which action is going to be made, the player explains it self, same with amount.
     */
    public static void editBalance(EcoAction action, Player player, long amount){
        if(action == EcoAction.WITHDRAW){
            if(amount > getBalance(player)){
                return;
            }
            Hikari.updateBalance(action, player, amount);

        }
        else if(action == EcoAction.SET){
            if(amount < 0){
                return;
            }
            Hikari.updateBalance(action, player, amount);
        }
        else{
            Hikari.updateBalance(action, player, amount);
        }
    }

    /**
     *
     * @param player
     * @return
     */
    public static long getBalance(Player player){
        if(!GemsCore.getAccounts().containsKey(player.getUniqueId())){
            return 0;
        }
        return GemsCore.getAccounts().get(player.getUniqueId());
    }


}
