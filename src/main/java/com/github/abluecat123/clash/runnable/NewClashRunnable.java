package com.github.abluecat123.clash.runnable;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.ConfigHandler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NewClashRunnable extends BukkitRunnable {

    private final Clash plugin;
    private final Player sender;
    private final Player target;

    public NewClashRunnable(Clash plugin, Player sender, Player target) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
    }

    @Override
    synchronized public void run() {
        new CountdownTipRunnable(ConfigHandler.countdown_time, this.plugin, this.sender, this.target).start();
        new TeleportRunnable(plugin, sender, target).start(ConfigHandler.countdown_time);
    }

    public void start()
    {
        this.runTask(this.plugin);
    }
}
