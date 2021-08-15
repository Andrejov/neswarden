package me.andrejov.neswarden;

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

        FileConfiguration config = this.getConfig();

        config.options().copyDefaults(true);
        config.addDefault("broadcast-prefix", "&7\\...&9&lnes.&7.../ &f: ");

        this.saveConfig();
    }

    @Override
    public void onDisable() {

    }
}