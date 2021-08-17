package me.andrejov.neswarden;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CustomCommand extends Executor {

    public CustomCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {
        if(lcname.equals("ncc") || lcname.equals("uc"))
        {
            if(plugin.getUtil().permCheck(sender, "neswarden.uc"))
            {
                return true;
            }

            if(args.length < 1)
            {
                plugin.getUtil().usage(sender, lcname, new String[] {"<name>"}, 0);
                return true;
            }

            String keyname = "uc-" + args[0].toLowerCase();

            List<String> cmds = (List<String>) plugin.getConfig().getList(keyname);

            if(cmds == null)
            {
                plugin.getUtil().usage(sender, lcname, new String[] {"<name>"}, 0);
                return true;
            }

            if(plugin.getUtil().permCheck(sender, "neswarden.uc." + args[0].toLowerCase()))
            {
                return true;
            }

            for (String cmd : cmds) {
                plugin.getUtil().execString(cmd, sender, args);
            }

            return true;
        }

        return false;
    }
    
}
