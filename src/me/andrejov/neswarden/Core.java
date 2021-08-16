package me.andrejov.neswarden;

import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin
{
    @Override
    public void onEnable() {
        this.getCommand("neswarden").setExecutor(new WardenCommand(this));
        this.getCommand("nw").setExecutor(new WardenCommand(this));

        this.getCommand("nbr").setExecutor(new BroadcastCommand(this));
        this.getCommand("npbr").setExecutor(new BroadcastCommand(this));
        this.getCommand("npmsg").setExecutor(new BroadcastCommand(this));

        this.getCommand("spawn").setExecutor(new WorldCommand(this));

        this.getCommand("logic").setExecutor(new LogicCommand(this));
        this.getCommand("and").setExecutor(new LogicCommand(this));

        FileConfiguration config = this.getConfig();

        config.options().copyDefaults(true);
        config.addDefault("broadcast-prefix", "&7\\...&9&lnes.&7.../ &f: ");
        config.addDefault("disable-command", Arrays.asList());
        config.addDefault("disable-response", "@npmsg %player% &c:3");

        this.saveConfig();

        new DisableHandler(this);
    }

    @Override
    public void onDisable() {

    }
}