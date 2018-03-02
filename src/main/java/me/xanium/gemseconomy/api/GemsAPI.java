package me.xanium.gemseconomy.api;

import me.xanium.gemseconomy.GemsCore;
import me.xanium.gemseconomy.backend.Hikari;
import me.xanium.gemseconomy.backend.UserConfig;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by John on 11.06.2017.
 **/
public class GemsAPI {

    /**
     * @param action
     * What type of transaction are you doing.
     * @param player
     * Player that receives the modification.
     * @param amount
     * Amount of gems to edit.
     */
    public static void editBalance(EcoAction action, Player player, double amount){
        if(action == EcoAction.WITHDRAW){
            if(amount > getBalance(player.getUniqueId())){
                return;
            }
            Hikari.updateBalance(action, player.getUniqueId(), amount, false);

        }
        else if(action == EcoAction.SET){
            if(amount < 0){
                return;
            }
            Hikari.updateBalance(action, player.getUniqueId(), amount, false);
        }
        else{
            Hikari.updateBalance(action, player.getUniqueId(), amount, false);
        }
    }

    /**
     * @param action
     * What type of transaction are you doing.
     * @param uuid
     * Player that receives the modification.
     * @param amount
     * Amount of gems to edit.
     */
    public static boolean editBalance(EcoAction action, UUID uuid, double amount, boolean offline){
        if(action == EcoAction.WITHDRAW){
            if(amount > getBalance(uuid)){
                return false;
            }
            Hikari.updateBalance(action, uuid, amount, offline);
            return true;
        }
        else if(action == EcoAction.SET){
            if(amount < 0){
                return false;
            }
            Hikari.updateBalance(action, uuid, amount, offline);
            return true;
        }
        else{
            Hikari.updateBalance(action, uuid, amount, offline);
            return true;
        }
    }

    /**
     * @param uuid
     * The unique id for the player.
     * @return
     * Amount of gems.
     */
    public static double getBalance(UUID uuid){
        if(!GemsCore.getAccounts().containsKey(uuid)){
            if(!GemsCore.isHikari()) {
                if (UserConfig.getInstance().getConfig(uuid) != null) {
                    return UserConfig.getInstance().getConfig(uuid).getDouble("Balance");
                }
                return -1;
            }else{
                return Hikari.getBalance(uuid);
            }
        }
        return GemsCore.getAccounts().get(uuid);
    }

    /**
     * @param player
     * The Player object.
     * @return
     * Amount of gems.
     */
    public static double getBalance(Player player){
        if(!GemsCore.getAccounts().containsKey(player.getUniqueId())){
            if(!GemsCore.isHikari()) {
                if (UserConfig.getInstance().getConfig(player.getUniqueId()) != null) {
                    return UserConfig.getInstance().getConfig(player.getUniqueId()).getDouble("Balance");
                }
                return -1;
            }else{
                return Hikari.getBalance(player.getUniqueId());
            }
        }
        return GemsCore.getAccounts().get(player.getUniqueId());
    }


}
