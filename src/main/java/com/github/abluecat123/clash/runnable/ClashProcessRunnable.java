package com.github.abluecat123.clash.runnable;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.ConfigHandler;
import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.util.ClashUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ClashProcessRunnable extends BukkitRunnable {

    private final Clash plugin;
    private final Player sender;
    private final Player target;
    private final double money;

    private BossBar bossBar;
    private long period;
    private long total;
    public ClashProcessRunnable(Clash plugin, Player sender, Player target, double money) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
        this.money = money;
    }

    @Override
    public void run() {
        total += period;
        bossBar.setTitle(setDistance(LangHandler.bossbar_title, sender, target));
        double pro = total * 1.0 / (20 * ConfigHandler.clash_total_time);
        if (pro > 1.0)
        {
            pro = 1.0;
        }
        bossBar.setProgress(pro);
    }

    public void start(long delay, long period) {
        this.period = period;
        this.total = 0;
        bossBar = Bukkit.createBossBar(Clash.getBOSSBAR(), setDistance(LangHandler.bossbar_title, sender, target), BarColor.PINK, BarStyle.SEGMENTED_12, BarFlag.PLAY_BOSS_MUSIC);
        bossBar.addPlayer(sender);
        bossBar.addPlayer(target);
        this.runTaskTimer(this.plugin, delay * 20L, period);
    }

    private static @NotNull String setDistance(String str, Player p1, Player p2)
    {
        if (Clash.usePAPI)
        {
            PlaceholderAPI.setPlaceholders(p1, str);
        }
        Location l1 = p1.getLocation();
        Location l2 = p2.getLocation();
        int dis = (int)Math.sqrt((l1.getX() - l2.getX()) * (l1.getX() - l2.getX()) +
                (l1.getY() - l2.getY()) * (l1.getY() - l2.getY()) +
                (l1.getZ() - l2.getZ()) * (l1.getZ() - l2.getZ()));
        str = str.replaceAll("%clash_distance%", String.valueOf(dis));
        return str;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        bossBar.removeAll();
        ClashUtil.clash_record.entrySet().removeIf(entry -> entry.getKey().first().getUniqueId().equals(sender.getUniqueId()) || entry.getKey().second().getUniqueId().equals(sender.getUniqueId()));
        super.cancel();
    }

    public double getMoney()
    {
        return money;
    }
}
