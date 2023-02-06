package com.github.abluecat123.clash.runnable;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.util.ClashUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ClashCancelRunnable extends BukkitRunnable {

    private final ClashProcessRunnable process;
    private final Clash plugin;
    private final Player sender;
    private final Player target;

    public ClashCancelRunnable(Clash plugin, ClashProcessRunnable process, Player sender, Player target) {
        this.plugin = plugin;
        this.process = process;
        this.sender = sender;
        this.target = target;
    }

    @Override
    public void run() {
        if (!process.isCancelled())
        {
            process.cancel();
            ClashUtil.clashEndProcess(sender, target, target, process.getMoney());
        }
    }

    public void start(long delay)
    {
        this.runTaskLater(plugin, delay * 20L);
    }
}
