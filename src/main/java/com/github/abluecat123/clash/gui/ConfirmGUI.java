package com.github.abluecat123.clash.gui;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.util.ClashUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ConfirmGUI {
    public static void createConfirmGUI(Player sender, Player target)
    {
        Inventory inventory = Bukkit.createInventory(sender, 5 * 9, setPlaceHolder(sender, LangHandler.confirm_screen_title));
        ItemStack confirm = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = confirm.getItemMeta();
        assert meta != null;
        meta.setDisplayName(setPlaceHolder(target, LangHandler.confirm_item_title));
        meta.setLore(ClashUtil.setPlaceHolderForLore(target, LangHandler.confirm_lore));
        meta.getPersistentDataContainer().set(Clash.getTargetUuid(), PersistentDataType.STRING, target.getUniqueId().toString());
        confirm.setItemMeta(meta);
        inventory.setItem(22, confirm);
        sender.openInventory(inventory);
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
