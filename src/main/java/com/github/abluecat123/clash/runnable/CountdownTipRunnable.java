package com.github.abluecat123.clash.runnable;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.LangHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTipRunnable extends BukkitRunnable {

    private int countdown;
    private final Clash plugin;
    private final Player sender;
    private final Player target;

    public CountdownTipRunnable(int countdown, Clash plugin, Player sender, Player target) {
        this.countdown = countdown;
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
    }

    @Override
    public void run() {
        if (this.countdown == 0)
        {
            sender.resetTitle();
            target.resetTitle();
            this.cancel();
            return;
        }
        sender.sendTitle(setPlaceHolder(target, LangHandler.clash_main_title_for_sender, this.countdown),
                setPlaceHolder(target, LangHandler.clash_subtitle_for_sender, this.countdown),
                5, 10, 5);
        target.sendTitle(setPlaceHolder(sender, LangHandler.clash_main_title_for_target, this.countdown),
                setPlaceHolder(sender, LangHandler.clash_subtitle_for_target, this.countdown),
                5, 10, 5);
        sender.getWorld().playSound(sender.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        target.getWorld().playSound(target.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        this.countdown -= 1;
    }

    private static String setPlaceHolder(Player player, String str, int countdown)
    {
        str = str.replaceAll("%countdown%", String.valueOf(countdown));
        if (Clash.usePAPI)
        {
            return PlaceholderAPI.setPlaceholders(player, str);
        }
        else
        {
            return str;
        }
    }

    public void start()
    {
        this.runTaskTimer(this.plugin, 0, 20);
    }
}
