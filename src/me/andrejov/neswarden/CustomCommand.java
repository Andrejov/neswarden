package me.andrejov.neswarden;

import java.util.List;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CustomCommand extends Executor {

    public CustomCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {
        if(lcname.equals("ncc") || lcname.equals("uc"))
        {
            if(Util.permCheck(sender, "neswarden.uc"))
            {
                return true;
            }

            if(args.length < 1)
            {
                Util.usage(sender, lcname, new String[] {"<name>"}, 0);
                return true;
            }

            String keyname = "uc-" + args[0];

            List<String> cmds = (List<String>) plugin.getConfig().getList(keyname);

            if(cmds == null)
            {
                Util.usage(sender, lcname, new String[] {"<name>"}, 0);
                return true;
            }

            for (String cmd : cmds) {
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

            return true;
        }

        return false;
    }
    
}
