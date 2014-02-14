package com.dsh105.vortex;

import com.dsh105.dshutils.DSHPlugin;
import com.dsh105.dshutils.Metrics;
import com.dsh105.dshutils.Updater;
import com.dsh105.dshutils.util.VersionUtil;
import com.dsh105.dshutils.command.CustomCommand;
import com.dsh105.dshutils.config.YAMLConfig;
import com.dsh105.dshutils.config.YAMLConfigManager;
import com.dsh105.vortex.commands.CommandComplete;
import com.dsh105.vortex.commands.VortexCommand;
import com.dsh105.vortex.config.ConfigOptions;
import com.dsh105.dshutils.logger.ConsoleLogger;
import com.dsh105.dshutils.logger.Logger;
import com.dsh105.vortex.util.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R1.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;


public class VortexPlugin extends DSHPlugin {

    private YAMLConfig config;
    private YAMLConfig langConfig;

    public ChatColor primaryColour = ChatColor.GOLD;
    public ChatColor secondaryColour = ChatColor.YELLOW;
    public String prefix = "" + ChatColor.DARK_RED + "[" + ChatColor.RED + "Vortex" + ChatColor.DARK_RED + "] " + ChatColor.RESET;
    public String cmdString = "vortex";

    public CommandMap CM;

    // Update data
    public boolean update = false;
    public String name = "";
    public long size = 0;
    public boolean updateChecked = false;

    @Override
    public void onEnable() {
        super.onEnable();
        Logger.initiate(this, "Vortex", "[Vortex]");

        if (!VersionUtil.compareVersions()) {
            ConsoleLogger.log(Logger.LogLevel.NORMAL, this.secondaryColour + "VortexPlugin " + this.primaryColour
                    + this.getDescription().getVersion() + this.secondaryColour + " is only compatible with:");
            ConsoleLogger.log(Logger.LogLevel.NORMAL, this.primaryColour + "    " + VersionUtil.getMinecraftVersion() + "-" + VersionUtil.getCraftBukkitVersion() + ".");
            ConsoleLogger.log(Logger.LogLevel.NORMAL, this.secondaryColour + "Initialisation failed. Please update the plugin.");
            return;
        }

        String[] header = {"Vortex By DSH105", "---------------------",
                "Configuration File",
                "See the Vortex Wiki before editing this file"};
        try {
            config = this.getConfigManager().getNewConfig("config.yml", header);
            new ConfigOptions(config);
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Failed to generate Configuration File (config.yml).", e, true);
        }

        config.reloadConfig();

        ChatColor colour1 = ChatColor.getByChar(this.getConfig(ConfigType.MAIN).getString("primaryChatColour", "6"));
        if (colour1 != null) {
            this.primaryColour = colour1;
        }
        ChatColor colour2 = ChatColor.getByChar(this.getConfig(ConfigType.MAIN).getString("secondaryChatColour", "e"));
        if (colour2 != null) {
            this.secondaryColour = colour2;
        }

        try {
            langConfig = this.getConfigManager().getNewConfig("language.yml", new String[] {"Vortex By DSH105", "---------------------", "Language Configuration File"});
            try {
                for (Lang l : Lang.values()) {
                    String[] desc = l.getDescription();
                    langConfig.set(l.getPath(), langConfig.getString(l.getPath(), l.toString_()
                            .replace("&6", "&" + this.primaryColour.getChar())
                            .replace("&e", "&" + this.secondaryColour.getChar())),
                            desc);
                }
                langConfig.saveConfig();
            } catch (Exception e) {
                Logger.log(Logger.LogLevel.SEVERE, "Failed to generate Configuration File (language.yml).", e, true);
            }

        } catch (Exception e) {
        }
        langConfig.reloadConfig();

        try {
            if (Bukkit.getServer() instanceof CraftServer) {
                final Field f = CraftServer.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                CM = (CommandMap) f.get(Bukkit.getServer());
            }
        } catch (Exception e) {
            Logger.log(Logger.LogLevel.SEVERE, "Command registration has failed.", e, true);
        }

        String cmdString = this.config.getString("command", "vortex");
        if (CM.getCommand(cmdString) != null) {
            Logger.log(Logger.LogLevel.WARNING, "A command under the name " + ChatColor.RED + "/" + cmdString + ChatColor.YELLOW + " already exists. Command temporarily registered under " + ChatColor.RED + "/v:" + cmdString, true);
        }

        CustomCommand cmd = new CustomCommand(cmdString);
        CM.register(this.cmdString, cmd);
        cmd.setExecutor(new VortexCommand(cmdString));
        cmd.setTabCompleter(new CommandComplete());
        this.cmdString = cmdString;

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            Logger.log(Logger.LogLevel.WARNING, "Plugin Metrics (MCStats) has failed to start.", e, false);
        }

        this.checkUpdates();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static VortexPlugin getInstance() {
        return (VortexPlugin) getPluginInstance();
    }

    protected void checkUpdates() {
        if (this.getConfig(ConfigType.MAIN).getBoolean("checkForUpdates", true)) {
            final File file = this.getFile();
            final Updater.UpdateType updateType = this.getConfig(ConfigType.MAIN).getBoolean("autoUpdate", false) ? Updater.UpdateType.DEFAULT.DEFAULT : Updater.UpdateType.NO_DOWNLOAD;
            getServer().getScheduler().runTaskAsynchronously(this, new Runnable() {
                @Override
                public void run() {
                    Updater updater = new Updater(getInstance(), 67541, file, updateType, false);
                    update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE;
                    if (update) {
                        name = updater.getLatestName();
                        ConsoleLogger.log(ChatColor.GOLD + "An update is available: " + name);
                        ConsoleLogger.log(ChatColor.GOLD + "Type /vupdate to update.");
                        if (!updateChecked) {
                            updateChecked = true;
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("vupdate")) {
            if (sender.hasPermission("vortex.update")) {
                if (updateChecked) {
                    @SuppressWarnings("unused")
                    Updater updater = new Updater(this, 67541, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
                    return true;
                } else {
                    sender.sendMessage(this.prefix + primaryColour + " An update is not available.");
                    return true;
                }
            } else {
                Lang.sendTo(sender, Lang.NO_PERMISSION.toString().replace("%perm%", "overcast.update"));
                return true;
            }
        }
        return false;
    }

    public YAMLConfig getConfig(ConfigType configType) {
        if (configType == ConfigType.MAIN) {
            return this.config;
        } else if (configType == ConfigType.LANG) {
            return this.langConfig;
        }
        return null;
    }

    public enum ConfigType {
        MAIN, LANG
    }
}