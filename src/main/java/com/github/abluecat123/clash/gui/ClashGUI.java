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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Collection;

public class ClashGUI {

    public static void openGUIPage(Player player, int page)
    {
        Collection<? extends Player> collection = Bukkit.getOnlinePlayers();
        Player[] onlinePlayers = collection.toArray(new Player[0]);
        Arrays.sort(onlinePlayers, (p1, p2) -> {
            if (Clash.useVault)
            {
                double money1 = Clash.getEconomy().getBalance(p1);
                double money2 = Clash.getEconomy().getBalance(p2);
                return money1 - money2 < 0 ? 1 : -1;
            }
            return p1.getName().compareTo(p2.getName());
        });
        while ((page - 1) * 28 > onlinePlayers.length)
        {
            page--;
        }
        if (page == 0)
        {
            page = 1;
        }
        int l = (page - 1) * 28;
        createClashGUI(player, onlinePlayers, l, page);
    }

    public static void createClashGUI(Player player, Player[] onlinePlayers, int l, int page)
    {
        Inventory inventory = Bukkit.createInventory(player, 6 * 9, setPlaceHolder(player, LangHandler.clash_screen_title));
        ItemStack blackGlass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 9; i++)
        {
            inventory.setItem(i, blackGlass);
        }
        for (int i = 1; i <= 4; i++)
        {
            inventory.setItem(i * 9, blackGlass);
            inventory.setItem(i * 9 + 8, blackGlass);
        }
        for (int i = 45; i <= 53; i++)
        {
            inventory.setItem(i, blackGlass);
        }
        ItemStack turnPageLeft = new ItemStack(Material.BIRCH_BOAT);
        ItemMeta turnPageLeftMeta = turnPageLeft.getItemMeta();
        assert turnPageLeftMeta != null;
        turnPageLeftMeta.setDisplayName(LangHandler.turn_page_left);
        turnPageLeftMeta.getPersistentDataContainer().set(Clash.getPAGE(), PersistentDataType.INTEGER, page - 1);
        turnPageLeft.setItemMeta(turnPageLeftMeta);
        inventory.setItem(47, turnPageLeft);

        ItemStack turnPageRight = new ItemStack(Material.BIRCH_BOAT);
        ItemMeta turnPageRightMeta = turnPageRight.getItemMeta();
        assert turnPageRightMeta != null;
        turnPageRightMeta.setDisplayName(LangHandler.turn_page_right);
        turnPageRightMeta.getPersistentDataContainer().set(Clash.getPAGE(), PersistentDataType.INTEGER, page + 1);
        turnPageRight.setItemMeta(turnPageRightMeta);
        inventory.setItem(51, turnPageRight);

        for (int i = 0; i < 28; i++)
        {
            int now = i + l;
            if (now >= onlinePlayers.length)
            {
                break;
            }
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            Player nowPlayer = onlinePlayers[now];
            assert skullMeta != null;
            skullMeta.setOwningPlayer(nowPlayer);
            skullMeta.setDisplayName(setPlaceHolder(nowPlayer, LangHandler.skull_title));
            skullMeta.setLore(ClashUtil.setPlaceHolderForLore(nowPlayer, LangHandler.skull_lore));
            skull.setItemMeta(skullMeta);
            inventory.setItem(getSlot(i), skull);
        }
        player.openInventory(inventory);
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

    private static int getSlot(int i)
    {
        if (i < 7)
        {
            return i + 10;
        }
        else if (i < 14)
        {
            return i + 12;
        }
        else if (i < 21)
        {
            return i + 14;
        }
        else
        {
            return i + 16;
        }
    }
}
