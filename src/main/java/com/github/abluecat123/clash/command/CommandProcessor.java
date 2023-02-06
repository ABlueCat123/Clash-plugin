package com.github.abluecat123.clash.command;

import com.github.abluecat123.clash.command.subcommands.CommandHelp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandProcessor implements TabExecutor {

    private final String[] subCommands = {"open", "help", "reload"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
        {
            new CommandHelp().executeCommand(sender, args);
            return true;
        }
        CommandProducer.getCommand(args[0]).executeCommand(sender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
        {
            return null;
        }
        if (args.length == 0)
        {
            return Arrays.asList(subCommands);
        }
        if (args.length == 1)
        {
            return Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
