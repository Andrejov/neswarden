package me.andrejov.neswarden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.ChatColor;

public class BroadcastCommand extends Executor {

    public BroadcastCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {

        if(Util.permCheck(sender, "neswarden.broadcast"))
        {
            return true;
        }
        
        String text = "";
        boolean global = false;
        CommandSender receiver = null;

        if(lcname.equals("nbr"))
        {
            text = String.join(" ", args);
            global = true;
        }else if(lcname.equals("npbr"))
        {
            text = plugin.getConfig().getString("broadcast-prefix") + String.join(" ", args);
            global = true;
        }else if(lcname.equals("npmsg"))
        {
            if(args.length < 2)
            {
                Util.usage(sender, lcname, new String[] {"<player>", "<msg>"}, args.length);
                return true;
            }

            receiver = plugin.getServer().getPlayer(args[0]);

            if(receiver == null)
            {
                Util.usage(sender, lcname, new String[] {"<player>", "<msg>"}, 0);
                return true;
            }

            text = String.join(" ", args).substring(args[0].length() + 1);
        }

        if(global)
        {
            this.plugin.getServer().broadcastMessage(
                ChatColor.translateAlternateColorCodes('&', text)
            );
            return true;
        }

        if(receiver != null)
        {
            receiver.sendMessage(
                ChatColor.translateAlternateColorCodes('&', text)
            );
            return true;
        }

        return false;
    }

}
