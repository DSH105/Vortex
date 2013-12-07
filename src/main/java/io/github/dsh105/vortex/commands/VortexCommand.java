package io.github.dsh105.vortex.commands;

import io.github.dsh105.vortex.VortexPlugin;
import io.github.dsh105.vortex.environment.tornado.Tornado;
import io.github.dsh105.vortex.environment.volcano.Volcano;
import io.github.dsh105.vortex.environment.whirly.Whirly;
import io.github.dsh105.vortex.util.Geometry;
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
    ChatColor c1 = VortexPlugin.getInstance().primaryColour;
    ChatColor c2 = VortexPlugin.getInstance().secondaryColour;

    public VortexCommand(String name) {
        this.label = name;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("tornado")) {
                    Player p = (Player) sender;
                    new Tornado(p.getLocation(), new Vector(Geometry.generateRandomFloat(0F, 1F), 0, Geometry.generateRandomFloat(0F, 1F)), 0.15F, 20*20, 25, 50);
                    return true;
                } else if (args[0].equalsIgnoreCase("whirly")) {
                    Player p = (Player) sender;
                    new Whirly(p.getLocation(), 0.15F, 20*40, 8);
                    return true;
                } else if (args[0].equalsIgnoreCase("volcano")) {
                    Player p = (Player) sender;
                    new Volcano(p.getLocation(), 20, 20*60, true);
                }
            }
        }
        Lang.sendTo(sender, Lang.COMMAND_ERROR.toString()
                .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
        return true;
    }
}