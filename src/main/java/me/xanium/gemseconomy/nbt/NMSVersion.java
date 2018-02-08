/*
 * Copyright JohnCrafted (c) 2017. All Rights Reserved.
 * Any code contained within this document, and any associated APIs with similar branding
 * are the sole property of JohnCrafted. Distribution, reproduction, taking snippets or claiming
 * any contents as your own will break the terms of the license, and void any agreements with you, the third party.
 * Thank you.
 */

package me.xanium.gemseconomy.nbt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by John on 08.05.2017.
 **/
public class NMSVersion {

    public static final String UNSUPPORTED = "Unsupported";

    public static final String V1_7_R1 = "v1_7_R1";

    public static final String V1_7_R2 = "v1_7_R2";

    public static final String V1_7_R3 = "v1_7_R3";

    public static final String V1_7_R4 = "v1_7_R4";

    public static final String V1_8_R1 = "v1_8_R1";

    public static final String V1_8_R2 = "v1_8_R2";

    public static final String V1_8_R3 = "v1_8_R3";

    public static final String V1_9_R1 = "v1_9_R1";

    public static final String V1_9_R2 = "v1_9_R2";

    public static final String V1_10_R1 = "v1_10_R1";

    public static final String V1_11_R1 = "v1_11_R1";

    public static final String V1_12_R1 = "v1_12_R1";

    private Map<Integer, String> versionMap;

    private int versionID;

    public int getVersionID() {
        return versionID;
    }

    public NMSVersion() {
        this.versionMap = new HashMap<>();
        this.loadVersions();

        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (this.versionMap.containsValue(version)) {
            this.versionID = getVersionID(version);
        } else {
            this.versionID = 0;
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "----------------------------------------------------------");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "YOU ARE RUNNING AN UNSUPPORTED VERSION OF SPIGOT!");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "DiscPlayer functionality will at best be limited. Please don't come");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "complaining to me, the developer of DiscPlayer, when something breaks,");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "because running an unsupported version will cause exactly this. I do");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "in no way accept responsibility for ANY damage caused to a server running");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "an unsupported version of Spigot. It is recommended that you change to");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "a supported version of Spigot. Supported versions are 1.7*, 1.8*, 1.9,");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "1.10, 1.11, 1.12. Versions marked with an asterisk (*) may have limited functionality.");
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "----------------------------------------------------------");
        }
    }

    private void loadVersions() {
        registerVersion(UNSUPPORTED);
        registerVersion(V1_7_R1);
        registerVersion(V1_7_R2);
        registerVersion(V1_7_R3);
        registerVersion(V1_7_R4);
        registerVersion(V1_8_R1);
        registerVersion(V1_8_R2);
        registerVersion(V1_8_R3);
        registerVersion(V1_9_R1);
        registerVersion(V1_9_R2);
        registerVersion(V1_10_R1);
        registerVersion(V1_11_R1);
        registerVersion(V1_12_R1);
    }

    private void registerVersion(String string) {
        this.versionMap.put(this.versionMap.size(), string);
    }

    public String getVersionString() {
        return this.getVersionString(this.versionID);
    }

    public boolean isLegacy() {
        return this.getVersionString().equals("v1_8_R1") || this.getVersionString().equals("v1_8_R2") || this.getVersionString().equals("v1_8_R3") || this.getVersionString().equals("v1_7_R1")
                || this.getVersionString().equals("v1_7_R2")||this.getVersionString().equals("v1_7_R3") || this.getVersionString().equals("v1_7_R4");
    }

    public String getVersionString(int id) {
        return this.versionMap.get(id);
    }

    public int getVersionID(String version) {
        return this.versionMap.entrySet().parallelStream()
                .filter(e -> e.getValue().equalsIgnoreCase(version))
                .map(Map.Entry::getKey).findFirst().orElse(0);
    }

    public boolean runningNewerThan(String version) {
        return this.versionID >= this.getVersionID(version);
    }
}
