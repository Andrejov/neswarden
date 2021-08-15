package me.andrejov.neswarden;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand extends Executor {
    
    public WorldCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {

        if(Util.permCheck(sender, "neswarden.alias"))
        {
            return true;
        }
        
        if(sender instanceof Player)
        {
            Player p = (Player) sender;

            String keyname = "alias-" + lcname + "-" + p.getWorld().getName();
            String defname = "alias-" + lcname;

            String cmd = null;

            cmd = plugin.getConfig().getString(keyname);

            if(cmd == null)
            {
                cmd = plugin.getConfig().getString(defname);
            }

            if(cmd == null)
            {
                sender.sendMessage(
                    ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("broadcast-prefix") +
                        "This command is not registered in config or you may not have enough permissions to use it"
                    )
                );
                return true;
            }

            boolean console = false;

            if(cmd.startsWith("@"))
            {
                console = true;
                cmd = cmd.substring(1);
            }

            cmd = cmd.replace("%player%", p.getName());
            cmd = cmd.replace("%world%", p.getWorld().getName());

            plugin.getLogger().info("Executing \'" + cmd + "\'' as " + (console ? "console" : "player"));

            if(console)
            {
                plugin.getServer().dispatchCommand(
                    plugin.getServer().getConsoleSender(),
                    cmd
                );
            }else{
                plugin.getServer().dispatchCommand(
                    sender,
                    cmd
                );
            }
            return true;
        }else{
            sender.sendMessage(ChatColor.RED + "[NW] This command can only be used by players");
            return true;
        }
    }

}
