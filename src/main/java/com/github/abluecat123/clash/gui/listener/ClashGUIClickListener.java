package com.github.abluecat123.clash.gui.listener;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.gui.ClashGUI;
import com.github.abluecat123.clash.gui.ConfirmGUI;
import com.github.abluecat123.clash.util.ClashUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ClashGUIClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!player.getOpenInventory().getTitle().equalsIgnoreCase(LangHandler.clash_screen_title))
        {
            return;
        }
        if (event.getRawSlot() < 0 || event.getRawSlot() > event.getInventory().getSize()) {
            return;
        }
        if (isClickedFrame(event.getRawSlot()))
        {
            event.setCancelled(true);
            return;
        }
        if (event.getRawSlot() == 47 || event.getRawSlot() == 51)
        {
            ItemStack item = event.getCurrentItem();
            Integer page = Objects.requireNonNull(Objects.requireNonNull(item).getItemMeta()).getPersistentDataContainer().get(Clash.getPAGE(), PersistentDataType.INTEGER);
            event.setCancelled(true);
            assert page != null;
            ClashGUI.openGUIPage(player, page);
            return;
        }
        event.setCancelled(true);
        ItemStack skull = event.getCurrentItem();
        if (skull == null)
        {
            return;
        }
        ItemMeta meta = skull.getItemMeta();
        if (meta instanceof SkullMeta) {
            SkullMeta skullMeta = (SkullMeta) meta;
            Player target = Objects.requireNonNull(skullMeta.getOwningPlayer()).getPlayer();
            if (target == null)
            {
                return;
            }
            if (!target.isOnline())
            {
                player.sendMessage(LangHandler.the_target_is_offline);
                player.closeInventory();
                return;
            }
            if (target.getUniqueId().equals(player.getUniqueId()))
            {
                player.sendMessage(LangHandler.you_cant_clash_yourself);
                player.closeInventory();
                return;
            }
            if (ClashUtil.checkIfPlayerInBannedWorld(target))
            {
                player.sendMessage(LangHandler.target_is_in_banned_world);
                player.closeInventory();
                return;
            }
            if (ClashUtil.isNewbie(target))
            {
                player.sendMessage(LangHandler.newbie_not_allowed_to_be_clashed.replaceAll("%clash_protection_time%", ClashUtil.getRemainSafeTime(target)));
                player.closeInventory();
                return;
            }
            if (ClashUtil.isDifferenceTooBig(player, target))
            {
                player.sendMessage(LangHandler.difference_is_too_big.replaceAll("%clash_different_time%", ClashUtil.getDifferentTime(target, player)));
                player.closeInventory();
                return;
            }
            ConfirmGUI.createConfirmGUI(player, target);
        }
    }

    private static boolean isClickedFrame(int i)
    {
        if (i == 47 || i == 51) return false;
        return (i <= 9 || i == 17 || i == 18 || i == 27 || i == 26 || i == 35 || i == 36 || i == 44 || i >= 45);
    }
}
