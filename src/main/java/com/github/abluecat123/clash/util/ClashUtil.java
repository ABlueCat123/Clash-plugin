package com.github.abluecat123.clash.util;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.ConfigHandler;
import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.runnable.ClashCancelRunnable;
import com.github.abluecat123.clash.runnable.ClashProcessRunnable;
import com.github.abluecat123.clash.runnable.NewClashRunnable;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class ClashUtil {

    public static Map<Pair<Player, Player>, ClashProcessRunnable> clash_record = new HashMap<>();

    public static boolean checkIfPlayerInBannedWorld(Player player)
    {
        if (ConfigHandler.disabled_world != null)
        {
            for (String worldName : ConfigHandler.disabled_world)
            {
                if (worldName.equalsIgnoreCase(player.getWorld().getName()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static void dispatchNewClash(@NotNull Player sender, @NotNull Player target, double money)
    {
        sender.setGlowing(true);
        target.setGlowing(true);
        new NewClashRunnable(Clash.getInstance(), sender, target).start();
        ClashProcessRunnable process = new ClashProcessRunnable(Clash.getInstance(), sender, target, money);
        process.start(ConfigHandler.countdown_time, 4);
        Pair<Player, Player> newPair = Pair.makePair(sender, target);
        clash_record.put(newPair, process);
        new ClashCancelRunnable(Clash.getInstance(), process, sender, target).start(ConfigHandler.countdown_time + ConfigHandler.clash_total_time);
    }

    public static void clashEndProcess(@NotNull Player sender, @NotNull Player target, @NotNull Player winner, double money)
    {
        sender.setGlowing(false);
        target.setGlowing(false);
        if (sender.getUniqueId().equals(winner.getUniqueId())) //胜利者是发起人
        {
            if (Clash.useVault)
            {
                double totMoney = money + money * ConfigHandler.award_rate;
                String winMes = LangHandler.clash_win_message.replaceAll("%clash_money_change%", String.valueOf(totMoney));
                String lossMes = LangHandler.clash_lose_message.replaceAll("%clash_money_change%", String.valueOf(money));
                sender.sendMessage(winMes);
                target.sendMessage(lossMes);
                Clash.getEconomy().depositPlayer(sender, totMoney);
                Clash.getEconomy().withdrawPlayer(target, money);
            }
            else
            {
                sender.sendMessage(LangHandler.clash_win_message);
                target.sendMessage(LangHandler.clash_lose_message);
            }
        }
        else
        {
            if (Clash.useVault)
            {
                double totMoney = money * ConfigHandler.award_rate;
                String winMes = LangHandler.clash_win_message.replaceAll("%clash_money_change%", String.valueOf(totMoney));
                String lossMes = LangHandler.clash_lose_message.replaceAll("%clash_money_change%", String.valueOf(money));
                target.sendMessage(winMes);
                sender.sendMessage(lossMes);
                Clash.getEconomy().depositPlayer(target, totMoney);
            }
            else
            {
                target.sendMessage(LangHandler.clash_win_message);
                sender.sendMessage(LangHandler.clash_lose_message);
            }
        }
    }

    public static boolean isNewbie(Player player)
    {
        if (!ConfigHandler.newbie_protection_enable)
        {
            return false;
        }
        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        return ticks < ConfigHandler.newbie_protection_protect_time * 20;
    }

    public static @NotNull String getRemainSafeTime(@NotNull Player player)
    {
        int remain = ConfigHandler.newbie_protection_protect_time - player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
        return String.valueOf(remain);
    }

    public static boolean isDifferenceTooBig(Player player, Player target) {
        if (!ConfigHandler.difference_protection_enable)
        {
            return false;
        }
        int difference = Math.abs((player.getStatistic(Statistic.PLAY_ONE_MINUTE) - target.getStatistic(Statistic.PLAY_ONE_MINUTE)) / 20);
        return difference > ConfigHandler.difference_protection_different_time;
    }

    public static @NotNull String getDifferentTime(@NotNull Player target, @NotNull Player player) {
        int difference = Math.abs((player.getStatistic(Statistic.PLAY_ONE_MINUTE) - target.getStatistic(Statistic.PLAY_ONE_MINUTE)) / 20);
        return String.valueOf(difference);
    }

    public static @Nullable ArrayList<String> setPlaceHolderForLore(Player player, @Nullable ArrayList<String> lores)
    {
        if (lores == null) return null;
        ArrayList<String> ret = new ArrayList<>();
        for (String lore : lores)
        {
            String tempLore = setPlaceHolder(player, lore);
            if (Clash.useVault)
            {
                double money = Clash.getEconomy().getBalance(player);
                double price = money * ConfigHandler.price_rate;
                BigDecimal bd = new BigDecimal(price);
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                price = Double.parseDouble(String.valueOf(bd));
                tempLore = tempLore.replaceAll("%clash_price%", String.valueOf(price));
            }
            ret.add(tempLore);
        }
        return ret;
    }

    private static String setPlaceHolder(Player player, String str)
    {
        if (Clash.usePAPI)
        {
            return PlaceholderAPI.setPlaceholders(player, str);
        }
        else
        {
            return str;
        }
    }
}
