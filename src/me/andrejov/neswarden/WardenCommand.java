package me.andrejov.neswarden;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WardenCommand extends Executor {

    public WardenCommand(Core plugin) {
        super(plugin);
    }

    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {
        if(!lcname.equals("nw") && !lcname.equals("neswarden"))
        {
            return false;
        }

        if(Util.permCheck(sender, "neswarden.admin"))
        {
            return true;
        }

        sender.sendMessage(
            ChatColor.translateAlternateColorCodes('&', "&9[NW] &7&oneswarden version &7") + plugin.getDescription().getVersion()
        );

        return true;
    }

}
