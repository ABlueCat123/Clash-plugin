package com.github.abluecat123.clash.command.subcommands;

import com.github.abluecat123.clash.config.LangHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandHelp implements ICommands{
    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("clash.help"))
        {
            sender.sendMessage(LangHandler.execute_command_denied);
            return;
        }
        if (LangHandler.regularhelp != null)
        {
            for (String str : LangHandler.regularhelp)
            {
                sender.sendMessage(str);
            }
        }
        if (sender.hasPermission("clash.ophelp") && LangHandler.ophelp != null)
        {
            for (String str : LangHandler.ophelp)
            {
                sender.sendMessage(str);
            }
        }
    }

    private static String toColor(String str)
    {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
