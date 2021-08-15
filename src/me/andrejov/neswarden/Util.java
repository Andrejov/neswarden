package me.andrejov.neswarden;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Util {
    
    public static void usage(CommandSender sender, String cmd, String[] args, int badArg)
    {
        String[] largs = new String[args.length];

        for(int i = 0; i < args.length; i++)
        {
            if(badArg == i)
            {
                largs[i] = ChatColor.YELLOW + "" + ChatColor.UNDERLINE + args[i] + ChatColor.RESET;
            }else{
                largs[i] = ChatColor.YELLOW + args[i] + ChatColor.RESET;
            }
        }

        String message = ChatColor.RED + "... Usage: " + 
            ChatColor.YELLOW + ChatColor.BOLD + "/" + cmd + " " +
            String.join(" ", largs);

        sender.sendMessage(message);
    }

    public static boolean permCheck(CommandSender sender, String perm)
    {
        if(sender.hasPermission(perm))
        {
            return false;
        }else{
            sender.sendMessage(
                ChatColor.RED + "... Missing perm: " +
                ChatColor.YELLOW + ChatColor.BOLD + perm.toUpperCase()
            );

            return true;
        }
    }

}
