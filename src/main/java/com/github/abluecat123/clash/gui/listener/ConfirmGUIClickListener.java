package com.github.abluecat123.clash.gui.listener;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.ConfigHandler;
import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.util.ClashUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public class ConfirmGUIClickListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (!player.getOpenInventory().getTitle().equalsIgnoreCase(LangHandler.confirm_screen_title))
        {
            return;
        }
        if (event.getRawSlot() < 0 || event.getRawSlot() > event.getInventory().getSize()) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null)
        {
            return;
        }
        String targetUUID = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(Clash.getTargetUuid(), PersistentDataType.STRING);
        UUID uuid = UUID.fromString(Objects.requireNonNull(targetUUID));
        Player target = Bukkit.getPlayer(uuid);
        if (target == null)
        {
            player.sendMessage(LangHandler.target_is_not_existed);
            return;
        }
        double price = 0;
        if (Clash.useVault)
        {
            double money = Clash.getEconomy().getBalance(target);
            price = money * ConfigHandler.price_rate;
            BigDecimal bd = new BigDecimal(price);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            price = Double.parseDouble(String.valueOf(bd));
            if (Clash.getEconomy().getBalance(player) < price)
            {
                player.sendMessage(LangHandler.money_is_not_enough_to_pay);
                player.closeInventory();
                event.setCancelled(true);
                return;
            }
            else {
                Clash.getEconomy().withdrawPlayer(player, price);
            }
        }
        event.setCancelled(true);
        player.closeInventory();
        ClashUtil.dispatchNewClash(player, target, price);
    }
}
