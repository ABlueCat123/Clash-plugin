package com.github.abluecat123.clash.runnable;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.ConfigHandler;
import com.github.abluecat123.clash.config.LangHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class TeleportRunnable extends BukkitRunnable {

    private final Clash plugin;
    private final Player sender;
    private final Player target;

    public TeleportRunnable(Clash plugin, Player sender, Player target) {
        this.plugin = plugin;
        this.sender = sender;
        this.target = target;
    }

    @Override
    public void run() {
        Location teleportLoc = target.getLocation();
        double minX = teleportLoc.getX() - ConfigHandler.teleport_radius;
        double maxX = teleportLoc.getX() + ConfigHandler.teleport_radius;
        double minZ = teleportLoc.getZ() - ConfigHandler.teleport_radius;
        double maxZ = teleportLoc.getZ() + ConfigHandler.teleport_radius;
        int newX = (int)(minX + Math.random() * (maxX - minX + 1));
        int newZ = (int)(minZ + Math.random() * (maxZ - minZ + 1));
        Location location = Objects.requireNonNull(teleportLoc.getWorld()).getHighestBlockAt(newX, newZ).getLocation();
        location.setY(location.getY() + 1.8);
        sender.teleport(location);
        sender.sendMessage(LangHandler.prepare_teleport);
        target.sendMessage(LangHandler.enemy_has_been_teleported);
    }

    public void start(long delay)
    {
        this.runTaskLater(this.plugin, delay * 20L);
    }
}
