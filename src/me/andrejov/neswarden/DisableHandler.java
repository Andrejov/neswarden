package me.andrejov.neswarden;

import java.util.Collection;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class DisableHandler implements Listener {
    
    Core plugin;

    public DisableHandler(Core plugin)
    {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandSend(PlayerCommandSendEvent event)
    {
        if(event.getPlayer().hasPermission("neswarden.disabled"))
        {
            return;
        }

        Collection<String> commands = event.getCommands();
        List<String> disabled = (List<String>) plugin.getConfig().getList("disable-command");

        // plugin.getLogger().info(commands.toArray()[0].toString());

        for (String cmd : disabled) {
            if(commands.contains(cmd))
            {
                commands.remove(cmd);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChatPost(AsyncPlayerChatEvent event)
    {
    }

    @EventHandler
    public void onPreprocess(PlayerCommandPreprocessEvent e)
    {
        if(e.getPlayer().hasPermission("neswarden.disabled"))
        {
            return;
        }

        List<String> disabled = (List<String>) plugin.getConfig().getList("disable-command");

        String cmd = e.getMessage().toLowerCase();
        // plugin.getLogger().info(cmd);

        if(cmd.startsWith("/")) cmd = cmd.substring(1);

        int spi = cmd.indexOf(" ");

        if(spi != -1) cmd = cmd.substring(0, spi);

        // plugin.getLogger().info(cmd);

        for (String string : disabled) {
            if(string.toLowerCase().equals(cmd))
            {
                String response = plugin.getConfig().getString("disable-response");
                // String response = plugin.getConfig().getString("disable-response")
                //     .replace("%player%", e.getPlayer().getName())
                //     .replace("%world%", e.getPlayer().getWorld().getName());
                // boolean console = false;

                // if(response.startsWith("@"))
                // {
                //     response = response.substring(1);
                //     console = true;
                // }

                // if(console)
                // {
                //     plugin.getServer().dispatchCommand(
                //         plugin.getServer().getConsoleSender(),
                //         response
                //     );
                // }else{
                //     plugin.getServer().dispatchCommand(
                //         e.getPlayer(),
                //         response
                //     );
                // }

                plugin.getUtil().execString(response, e.getPlayer());

                e.setCancelled(true);
                return;
            }
        }
    }

}
