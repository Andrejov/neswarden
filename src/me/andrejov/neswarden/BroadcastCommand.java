package me.andrejov.neswarden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class BroadcastCommand extends Executor {

    public BroadcastCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {
        
        boolean valid = false;
        String text = "";

        if(lcname.equals("nbr"))
        {
            text = String.join(" ", args);
            valid = true;
        }else if(lcname.equals("npbr"))
        {
            text = plugin.getConfig().getString("broadcast-prefix") + String.join(" ", args);
            valid = true;
        }

        if(valid)
        {
            this.plugin.getServer().broadcastMessage(
                ChatColor.translateAlternateColorCodes('&', text)
            );
            return true;
        }

        return false;
    }

}
