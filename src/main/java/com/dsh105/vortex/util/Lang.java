package com.dsh105.vortex.util;

import com.dsh105.vortex.VortexPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public enum Lang {

    NO_PERMISSION("no_permission", "&e%perm% &apermission needed to do that."),
    COMMAND_ERROR("cmd_error", "&aError for input string: &e%cmd%&a. Use &e/" + VortexPlugin.getInstance().cmdString + " help &afor help."),
    HELP_INDEX_TOO_BIG("help_index_too_big", "&aPage &e%index% &adoes not exist."),
    IN_GAME_ONLY("in_game_only", "&ePlease log in to do that."),
    STRING_ERROR("string_error", "&aError parsing String: [&e%string%&a]. Please revise command arguments."),
    NULL_PLAYER("null_player", "&e%player% &ais not online. Please try a different Player."),
    INT_ONLY("int_only", "&e%string% &a[Arg &e%argNum%&a] needs to be an integer."),
    ;

    private String path;
    private String def;
    private String[] desc;

    Lang(String path, String def, String... desc) {
        this.path = path;
        this.def = def;
        this.desc = desc;
    }

    public String[] getDescription() {
        return this.desc;
    }

    public String getPath() {
        return this.path;
    }

    public static void sendTo(CommandSender sender, String msg) {
        if (msg != null || !msg.equalsIgnoreCase("") && !msg.equalsIgnoreCase(" ") && !msg.equalsIgnoreCase("none")) {
            sender.sendMessage(VortexPlugin.getInstance().prefix + msg);
        }
    }

    public static void sendTo(Player p, String msg) {
        if (msg != null && !msg.equalsIgnoreCase("") && !msg.equalsIgnoreCase(" ") && !(msg.equalsIgnoreCase("none"))) {
            p.sendMessage(VortexPlugin.getInstance().prefix + msg);
        }
    }

    @Override
    public String toString() {
        String result = VortexPlugin.getInstance().getConfig(VortexPlugin.ConfigType.LANG).getString(this.path, this.def);
        if (result != null && result != "" && result != "none") {
            return ChatColor.translateAlternateColorCodes('&', VortexPlugin.getInstance().getConfig(VortexPlugin.ConfigType.LANG).getString(this.path, this.def));
        } else {
            return "";
        }
    }

    public String toString_() {
        return VortexPlugin.getInstance().getConfig(VortexPlugin.ConfigType.LANG).getString(this.path, this.def);
    }
}