package com.github.abluecat123.clash.command;

import com.github.abluecat123.clash.command.subcommands.*;

public class CommandProducer {
    public static ICommands getCommand(String commandType)
    {
        switch (commandType)
        {
            case "help":
                return new CommandHelp();
            case "open":
                return new CommandOpen();
            case "reload":
                return new CommandReload();
            default:
                return new CommandError();
        }
    }
}
