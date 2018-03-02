/*
 * Copyright Xanium Development (c) 2013-2018. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of Xanium Development. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.vault;

import me.xanium.gemseconomy.api.EcoAction;
import me.xanium.gemseconomy.api.GemsAPI;
import me.xanium.gemseconomy.utils.FormatUtil;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class GemsEconomy extends AbstractEconomy {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "GemsEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return FormatUtil.formatNumber(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        if(getBalance(player) >= amount){
            return true;
        }
        return false;
    }

    @Override
    public boolean hasAccount(String playerName) {
        if(Bukkit.getOfflinePlayer(playerName) != null){
            if(GemsAPI.getBalance(Bukkit.getOfflinePlayer(playerName).getUniqueId()) != -1){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public double getBalance(String playerName) {
        return GemsAPI.getBalance(Bukkit.getOfflinePlayer(playerName).getUniqueId());
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return GemsAPI.getBalance(player.getUniqueId());
    }



    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        if(amount < 0){
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Can't withdraw negative amounts.");
        }

        double balance = 0;
        EconomyResponse.ResponseType type = EconomyResponse.ResponseType.FAILURE;
        String error = null;

        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(player != null) {
            if(player.isOnline()) {
                if (GemsAPI.editBalance(EcoAction.WITHDRAW, player.getUniqueId(), amount, false)) {
                    balance = getBalance(player);
                    type = EconomyResponse.ResponseType.SUCCESS;
                } else {
                    balance = getBalance(player);
                    error = "Not enough money.";
                }
            }else{
                if (GemsAPI.editBalance(EcoAction.WITHDRAW, player.getUniqueId(), amount, true)) {
                    balance = getBalance(player);
                    type = EconomyResponse.ResponseType.SUCCESS;
                } else {
                    balance = getBalance(player);
                    error = "Not enough money.";
                }
            }
        }
        return new EconomyResponse(amount, balance, type, error);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        }

        double balance = 0;
        EconomyResponse.ResponseType type = EconomyResponse.ResponseType.FAILURE;
        String error = null;

        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(player != null) {
            if(player.isOnline()) {
                if (GemsAPI.editBalance(EcoAction.DEPOSIT, player.getUniqueId(), amount, false)) {
                    balance = getBalance(player);
                    type = EconomyResponse.ResponseType.SUCCESS;
                } else {
                    balance = getBalance(player);
                    error = "Could not deposit to " + playerName + " because something went wrong.";
                }
            }else{
                if (GemsAPI.editBalance(EcoAction.DEPOSIT, player.getUniqueId(), amount, true)) {
                    balance = getBalance(player);
                    type = EconomyResponse.ResponseType.SUCCESS;
                } else {
                    balance = getBalance(player);
                    error = "Could not deposit to " + playerName + " because something went wrong.";
                }
            }
        }
        return new EconomyResponse(amount, balance, type, error);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "GemsEconomy does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean has(String playerName, double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(player != null){
            return has(player, amount);
        }
        return false;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return hasAccount(player.getName());
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }


}
