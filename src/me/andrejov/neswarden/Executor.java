package me.andrejov.neswarden;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class Executor implements CommandExecutor {
    Core plugin;

    public Executor(Core plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String lcname = command.getName().toLowerCase();

        return this.execute(sender, command, lcname, label, args);
    }

    public abstract boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args);
}
