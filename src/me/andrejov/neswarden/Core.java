package me.andrejov.neswarden;

import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin
{
    JoinHandler joinHandler;
    DisableHandler disableHandler;
    TimeoutHandler timeoutHandler;

    Util util;

    @Override
    public void onEnable() {
        this.util = new Util(this);

        this.disableHandler = new DisableHandler(this);
        this.joinHandler = new JoinHandler(this);
        this.timeoutHandler = new TimeoutHandler(this);

        this.getCommand("neswarden").setExecutor(new WardenCommand(this));
        this.getCommand("nw").setExecutor(new WardenCommand(this));

        this.getCommand("nbr").setExecutor(new BroadcastCommand(this));
        this.getCommand("npbr").setExecutor(new BroadcastCommand(this));
        this.getCommand("npmsg").setExecutor(new BroadcastCommand(this));

        this.getCommand("spawn").setExecutor(new WorldCommand(this));

        this.getCommand("logic").setExecutor(new LogicCommand(this));
        this.getCommand("and").setExecutor(new LogicCommand(this));

        this.getCommand("ncc").setExecutor(new CustomCommand(this));
        this.getCommand("uc").setExecutor(new CustomCommand(this));

        this.getCommand("joins").setExecutor(joinHandler);

        this.getCommand("cancel").setExecutor(timeoutHandler);
        this.getCommand("to").setExecutor(timeoutHandler);

        FileConfiguration config = this.getConfig();

        config.options().copyDefaults(true);
        config.addDefault("broadcast-prefix", "&7\\...&9&lnes.&7.../ &f: ");
        config.addDefault("disable-command", Arrays.asList());
        config.addDefault("disable-response", "@npmsg %player% &c:3");

        this.saveConfig();
    }

    @Override
    public void onDisable() {

    }

    public Util getUtil()
    {
        return this.util;
    }
}