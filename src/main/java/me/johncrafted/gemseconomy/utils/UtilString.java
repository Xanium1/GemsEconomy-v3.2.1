package me.johncrafted.gemseconomy.utils;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by John on 22.08.2016.
 **/
public abstract class UtilString {

    public static String format(String message)
    {
        return message.replaceAll("(?i)&([a-z0-9])", "§$1");
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static List<String> format(List<String> list)
    {
        List<String> temp = new ArrayList<>();
        for (String line : list) {
            temp.add(format(line));
        }
        return temp;
    }
    public static String format(double value) {
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

        int max = 2;
        int min = 0;

        nf.setMaximumFractionDigits(max);
        nf.setMinimumFractionDigits(min);
        return nf.format(value);
    }

    public static String formatMoney(double money)
    {
        DecimalFormat format = new DecimalFormat("###,###.##");
        return format.format(money);
    }

    public static String formatNumber(int number)
    {
        DecimalFormat format = new DecimalFormat("###,###");
        return format.format(number);
    }

    public static String trim(String string, int number)
    {
        if (string.length() <= number) {
            return string;
        }
        return string.substring(0, number);
    }

    public static String toUpperCase(String string)
    {
        return string.toUpperCase();
    }

    public static String toLowerCase(String string)
    {
        return string.toLowerCase();
    }

    public static String shift(String string, int size)
    {
        char[] array = string.toCharArray();
        char firstChar = array[0];
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[(i + 1)];
        }
        array[(array.length - 1)] = firstChar;
        if (size == -1) {
            return build(array);
        }
        return build(array).substring(0, size);
    }

    public static String build(char[] array)
    {
        String string = "";
        for (char chr : array) {
            string = string + chr;
        }
        return string;
    }

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public static boolean isInteger(String s){
        try{
            Integer.parseInt(s);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public static String sentenceBuilder(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public static String colorize(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static List<String> colorize(List<String> message){
        List<String> colorizedList = Lists.newArrayList();
        for(String str : message){
            colorizedList.add(colorize(str));
        }
        return colorizedList;
    }

}
