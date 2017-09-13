package me.johncrafted.gemseconomy.utils;

import org.bukkit.command.CommandSender;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by John on 11.06.2017.
 **/
public class UtilNumber {

    public static boolean validateInput(CommandSender sender, String input) {
        long amount;
        try {

            amount = Long.parseLong(input);
            if (amount < 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {
            sender.sendMessage(Messages.getPrefix() + Messages.getUnvalidAmount());
            return false;
        }
        return true;
    }

    public static String formatDouble(double value) {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

        int max = 2;
        int min = 0;

        nf.setMaximumFractionDigits(max);
        nf.setMinimumFractionDigits(min);
        return nf.format(value);
    }
}
