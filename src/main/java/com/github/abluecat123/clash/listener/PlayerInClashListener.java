package com.github.abluecat123.clash.listener;

import com.github.abluecat123.clash.config.ConfigHandler;
import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.runnable.ClashProcessRunnable;
import com.github.abluecat123.clash.util.ClashUtil;
import com.github.abluecat123.clash.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class PlayerInClashListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        if (ConfigHandler.banned_command_in_fight == null)
        {
            return;
        }
        Player player = event.getPlayer();
        for (Map.Entry<Pair<Player, Player>, ClashProcessRunnable> entry : ClashUtil.clash_record.entrySet()) {
            if (entry.getKey().first().getUniqueId().equals(player.getUniqueId()) || entry.getKey().second().getUniqueId().equals(player.getUniqueId()))
            {
                for (String str : ConfigHandler.banned_command_in_fight)
                {
                    if (event.getMessage().contains(str))
                    {
                        player.sendMessage(LangHandler.player_use_banned_command_in_fight);
                        event.setCancelled(true);
                        return;
                    }
                }
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event.useItemInHand() == Event.Result.DENY)
        {
            return;
        }
        if (ConfigHandler.banned_item_material_in_fight == null)
        {
            return;
        }
        if (event.getItem() == null)
        {
            return;
        }
        Player player = event.getPlayer();
        for (Map.Entry<Pair<Player, Player>, ClashProcessRunnable> entry : ClashUtil.clash_record.entrySet()) {
            if (entry.getKey().first().getUniqueId().equals(player.getUniqueId()) || entry.getKey().second().getUniqueId().equals(player.getUniqueId()))
            {
                for (String str : ConfigHandler.banned_item_material_in_fight)
                {
                    if (event.getItem().getType() == Material.getMaterial(str))
                    {
                        player.sendMessage(LangHandler.player_use_banned_item_in_fight);
                        event.setCancelled(true);
                        return;
                    }
                }
                return;
            }
        }
    }
}
