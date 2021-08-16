package me.andrejov.neswarden;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {
    
    Core plugin;

    public Util(Core plugin)
    {
        this.plugin = plugin;
    }

    public void usage(CommandSender sender, String cmd, String[] args, int badArg)
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

    public boolean permCheck(CommandSender sender, String perm)
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

    public void execString(String cmd, CommandSender sender)
    {
        execString(cmd, sender, new String[] {});
    }

    public void execString(String cmd, CommandSender sender, String[] args)
    {
        boolean console = false;

        if(cmd.startsWith("@"))
        {
            console = true;
            cmd = cmd.substring(1);
        }

        if(sender instanceof Player)
        {
            Player p = (Player) sender;
            cmd = cmd.replace("%player%", p.getName());
            cmd = cmd.replace("%world%", p.getWorld().getName());
            
        }

        for(int ai = 0; ai < args.length; ai ++)
        {
            cmd = cmd.replace("%" + ai + "%", args[ai]);
        }

        Random r = new Random();

        while(cmd.contains("%r%"))
        {
            int i = r.nextInt(10);
            int io = cmd.indexOf("%r%");

            cmd = cmd.substring(0, io) + i + cmd.substring(io + 3);
        }
        while(cmd.contains("%c%"))
        {
            char c = (char) ('a' + r.nextInt(26));
            int io = cmd.indexOf("%c%");

            cmd = cmd.substring(0, io) + c + cmd.substring(io + 3);
        }

        CommandSender executor = sender;

        if(console)
        {
            executor = this.plugin.getServer().getConsoleSender();
        }

        this.plugin.getServer().dispatchCommand(
            executor,
            cmd
        );
    }

}
