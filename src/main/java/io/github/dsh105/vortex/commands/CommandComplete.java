package io.github.dsh105.vortex.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;


public class CommandComplete implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<String>();
        String cmdString = io.github.dsh105.vortex.VortexPlugin.getInstance().cmdString;
        if (cmd.getName().equalsIgnoreCase(cmdString)) {
            String[] completions;
            if (args.length >= 2) {
                completions = getCompletions(args.length, args[args.length - 2]);
            } else {
                completions = getCompletions(args.length);
            }
            for (String s : completions) {
                if (s.startsWith(args[args.length - 1])) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    private String[] getCompletions(int i) {
        switch (i) {
            case 0:
                return new String[]{io.github.dsh105.vortex.VortexPlugin.getInstance().cmdString};
            case 1:

        }
        return new String[0];
    }

    private String[] getCompletions(int i, String argBefore) {
        switch (i) {
            case 0:
                return getCompletions(i);
            case 1:
                return getCompletions(i);
            case 2:
                ArrayList<String> list = new ArrayList<String>();

                return list.toArray(new String[list.size()]);
        }
        return new String[0];
    }
}