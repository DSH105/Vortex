package io.github.dsh105.vortex.commands;

import io.github.dsh105.vortex.environment.tornado.Tornado;
import io.github.dsh105.vortex.util.Lang;
import io.github.dsh105.vortex.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VortexCommand implements CommandExecutor {

    public String label;
    ChatColor c1 = io.github.dsh105.vortex.VortexPlugin.getInstance().primaryColour;
    ChatColor c2 = io.github.dsh105.vortex.VortexPlugin.getInstance().secondaryColour;

    public VortexCommand(String name) {
        this.label = name;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("tornado")) {
                    Player p = (Player) sender;
                    new Tornado(p.getLocation(), new Vector(io.github.dsh105.vortex.VortexPlugin.r().nextFloat(), 0, io.github.dsh105.vortex.VortexPlugin.r().nextFloat()), 0.15F, 20*20, 25, 50);
                    return true;
                } else if (args[0].equalsIgnoreCase("whirly")) {
                    Player p = (Player) sender;
                    new Tornado(p.getLocation(), new Vector(io.github.dsh105.vortex.VortexPlugin.r().nextFloat(), 0, io.github.dsh105.vortex.VortexPlugin.r().nextFloat()), 0.15F, 20*10, 6, 12);
                    return true;
                }
            }
        }
        Lang.sendTo(sender, Lang.COMMAND_ERROR.toString()
                .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
        return true;
    }
}