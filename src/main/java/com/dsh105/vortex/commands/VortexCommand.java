package com.dsh105.vortex.commands;

import com.dsh105.vortex.environment.earthquake.Earthquake;
import com.dsh105.dshutils.util.StringUtil;
import com.dsh105.dshutils.util.GeometryUtil;
import com.dsh105.vortex.VortexPlugin;
import com.dsh105.vortex.environment.earthquake.ShakeEntity;
import com.dsh105.vortex.environment.tornado.Tornado;
import com.dsh105.vortex.environment.volcano.Volcano;
import com.dsh105.vortex.environment.whirly.Whirly;
import com.dsh105.vortex.util.Lang;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

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
                    new Tornado(p.getLocation(), new Vector(GeometryUtil.generateRandomFloat(0F, 1F), 0, GeometryUtil.generateRandomFloat(0F, 1F)), 0.15F, 20*20, 25, 80);
                    return true;
                } else if (args[0].equalsIgnoreCase("whirly")) {
                    Player p = (Player) sender;
                    for (int i = 1; i <=5; i++) {
                        new Whirly(p.getLocation(), 0.2F, 20*80, 4);
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("volcano")) {
                    Player p = (Player) sender;
                    new Volcano(p.getLocation(), 20, 20*60, true);
                } else if (args[0].equalsIgnoreCase("shake")) {
                    Player p = (Player) sender;
                    List<Entity> entityList = GeometryUtil.getNearbyEntities(p.getLocation(), 50);
                    if (entityList != null && !entityList.isEmpty()) {
                        for (Entity e : entityList) {
                            new ShakeEntity(null, e, 20 / 2, true);
                        }
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("earthquake")) {
                    Player p = (Player) sender;
                    Location l = p.getWorld().getHighestBlockAt(p.getLocation()).getLocation();
                    l.setY(l.getY() - 2);
                    new Earthquake(p.getLocation(), 50, 20*60);
                    return true;
                }
            }
        }
        Lang.sendTo(sender, Lang.COMMAND_ERROR.toString()
                .replace("%cmd%", "/" + cmd.getLabel() + " " + (args.length == 0 ? "" : StringUtil.combineSplit(0, args, " "))));
        return true;
    }
}