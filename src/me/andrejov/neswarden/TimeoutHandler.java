package me.andrejov.neswarden;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeoutHandler extends Executor {

    public HashMap<Player, Timeout> timeouts = new HashMap<>();

    public TimeoutHandler(Core plugin) {
        super(plugin);

        this.plugin = plugin;

        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(
            this.plugin,
            new Runnable() {
                public void run()
                {
                    plugin.timeoutHandler.timeoutLoop();
                }
            }, 
            3L, 10L
        );
    }

    boolean check = false;

    public void timeoutLoop()
    {
        if(!check) return;

        if(timeouts.size() > 0)
        {
            // inLoop = true;
            // plugin.getLogger().info("Pending timeout loop, entries: " + timeouts.size());

            for(Map.Entry<Player, Timeout> entry : timeouts.entrySet())
            {
                Player p = entry.getKey();
                Timeout t = entry.getValue();

                if(t.elapsed())
                {
                    // plugin.getLogger().info("elapsed for " + p.getName());
                    // Execute timeout
                    plugin.getUtil().execString(t.action, t.player);

                    timeouts.remove(p);
                }else{
                    // plugin.getLogger().info("not for " + p.getName());
                    if(!t.canMove && t.hasMoved())
                    {
                        // plugin.getLogger().info("moved for " + p.getName());
                        p.sendMessage(
                            ChatColor.YELLOW +
                            "Action has been cancelled because you have moved."
                        );
                        timeouts.remove(p);
                    }
                }
            }

            // TimeoutHandler th = this;

            // plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable(){
            //     public void run() {
            //             th.timeoutLoop();
            //     }
            // }, 10L);
        }else{
            check = false;
        }
        // else
        // {
        //     inLoop = false;
        //     plugin.getLogger().info("Closing timeout loop");
        // }
    }

    public Timeout registerTimeout(Player p, int time, String action, boolean canMove)
    {
        if(this.timeouts.get(p) != null)
        {
            return null;
        }

        Timeout t = new Timeout(p, time, action, canMove);

        this.timeouts.put(p, t);
        this.check = true;
        // this.timeoutLoop();

        return t;
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {
        
        if(lcname.equals("cancel"))
        {
            if(plugin.getUtil().permCheck(sender, "neswarden.cancel"))
            {
                return true;
            }

            Timeout t = this.timeouts.get(sender);

            if(t == null)
            {
                sender.sendMessage(ChatColor.RED + "You do not have any timeout to cancel right now.");
            }else{
                timeouts.remove(sender);

                sender.sendMessage(ChatColor.YELLOW + "The pending action has been cancelled.");
            }

            return true;
        }else if(lcname.equals("to"))
        {
            if(plugin.getUtil().permCheck(sender, "neswarden.timeout"))
            {
                return true;
            }

            String[] usage = new String[] {"<player>", "<timeout: ms>", "<canMove: 1|0|true|false>", "<command ...>"};

            if(args.length < 4)
            {
                plugin.getUtil().usage(sender, lcname, usage, args.length);
                return true;
            }

            Player destination = plugin.getServer().getPlayer(args[0]);

            if(destination == null)
            {
                plugin.getUtil().usage(sender, lcname, usage, 0);
                return true;
            }

            int timeout;

            try {
                timeout = Integer.parseInt(args[1]);
            } catch (Exception e) {
                plugin.getUtil().usage(sender, lcname, usage, 1);
                return true;
            }

            boolean canMove = false;

            if(args[2].toLowerCase().equals("true") ||
               args[2].equals("1"))
            {
                canMove = true;
            }

            List<String> argsl = new ArrayList<>(Arrays.asList(args));

            argsl.remove(0);
            argsl.remove(0);
            argsl.remove(0);

            String action = String.join(" ", argsl.toArray(new String[argsl.size()]));

            if(destination.hasPermission("neswarden.notimeout"))
            {
                plugin.getUtil().execString(action, destination);
                return true;
            }

            Timeout t = this.registerTimeout(destination, timeout, action, canMove);

            if(t == null)
            {
                destination.sendMessage(ChatColor.RED + "You have a pending request already.");
                sender.sendMessage(ChatColor.RED + "TO: failed - pending request");
            } else {
                destination.sendMessage(
                    ChatColor.GREEN + "Your action will be executed in " +
                    Math.ceil(timeout / 1000) + " s..."
                );
                destination.sendMessage(
                    ChatColor.GRAY + "" + ChatColor.ITALIC + 
                    "You can cancel it using " +
                    ChatColor.YELLOW + "" + ChatColor.BOLD + 
                    "/cancel"
                );
                sender.sendMessage(ChatColor.GREEN + "TO: OK");
            }

            return true;
        }
        
        return false;
    }
    
}
