package me.andrejov.neswarden;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class LogicCommand extends Executor {

    public LogicCommand(Core plugin) {
        super(plugin);
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {

        if(Util.permCheck(sender, "neswarden.logic"))
        {
            return true;
        }

        if(lcname.equals("logic") || lcname.equals("or") || lcname.equals("and"))
        {
            String text = String.join(" ", args);
            
            handleLogic(sender, text);

            return true;
        }

        return false;
    }

    private void handleLogic(CommandSender sender, String text)
    {
        int andi = text.indexOf("&&");
        if(andi != -1)
        {
            String c1 = text.substring(0, andi);
            String c2 = text.substring(andi + "&&".length());

            handleLogic(sender, c1);
            handleLogic(sender, c2);

            return;
        }

        this.plugin.getServer().dispatchCommand(
            sender,
            text
        );
    }
    
}
