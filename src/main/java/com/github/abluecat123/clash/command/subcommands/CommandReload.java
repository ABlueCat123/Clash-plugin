package com.github.abluecat123.clash.command.subcommands;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.ConfigHandler;
import com.github.abluecat123.clash.config.LangHandler;
import org.bukkit.command.CommandSender;

public class CommandReload implements ICommands{
    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("clash.reload"))
        {
            sender.sendMessage(LangHandler.execute_command_denied);
            return;
        }
        Clash.getInstance().reloadConfig();
        Clash.getInstance().reloadLang();

        ConfigHandler.reloadConfigSetting();
        LangHandler.reloadLangSetting();

        sender.sendMessage(LangHandler.reload_completed);
    }
}
