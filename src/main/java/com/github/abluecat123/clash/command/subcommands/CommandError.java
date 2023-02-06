package com.github.abluecat123.clash.command.subcommands;

import com.github.abluecat123.clash.config.LangHandler;
import org.bukkit.command.CommandSender;

public class CommandError implements ICommands{
    @Override
    public void executeCommand(CommandSender sender, String[] args) {
        sender.sendMessage(LangHandler.command_enter_error);
    }
}
