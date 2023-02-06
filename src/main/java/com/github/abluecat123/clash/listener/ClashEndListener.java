package com.github.abluecat123.clash.listener;

import com.github.abluecat123.clash.runnable.ClashProcessRunnable;
import com.github.abluecat123.clash.util.ClashUtil;
import com.github.abluecat123.clash.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class ClashEndListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        failedClash(event.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        failedClash(event.getPlayer());
    }

    private static void failedClash(Player player) {
        for (Map.Entry<Pair<Player, Player>, ClashProcessRunnable> entry : ClashUtil.clash_record.entrySet()) {
            if (entry.getKey().first().getUniqueId().equals(player.getUniqueId()) || entry.getKey().second().getUniqueId().equals(player.getUniqueId()))
            {
                Player winner = entry.getKey().first().getUniqueId().equals(player.getUniqueId()) ? entry.getKey().second() : entry.getKey().first();
                entry.getValue().cancel();
                ClashUtil.clashEndProcess(entry.getKey().first(), entry.getKey().second(), winner, entry.getValue().getMoney());
                return;
            }
        }
    }
}
