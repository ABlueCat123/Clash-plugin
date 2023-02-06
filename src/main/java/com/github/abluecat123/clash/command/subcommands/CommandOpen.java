package com.github.abluecat123.clash.command.subcommands;

import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.gui.ClashGUI;
import com.github.abluecat123.clash.util.ClashUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOpen implements ICommands{
    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("clash.open"))
        {
            sender.sendMessage(LangHandler.execute_command_denied);
            return;
        }
        if (!(sender instanceof Player))
        {
            sender.sendMessage(LangHandler.console_are_not_allowed_to_perform_this_command);
            return;
        }
        Player player = (Player) sender;
        if (ClashUtil.checkIfPlayerInBannedWorld(player))
        {
            player.sendMessage(LangHandler.this_world_has_been_banned);
            return;
        }
        if (ClashUtil.isNewbie(player))
        {
            player.sendMessage(LangHandler.newbie_not_allowed_to_clash.replaceAll("%clash_protection_time%", ClashUtil.getRemainSafeTime(player)));
            return;
        }
        ClashGUI.openGUIPage(player, 1);
    }
}
