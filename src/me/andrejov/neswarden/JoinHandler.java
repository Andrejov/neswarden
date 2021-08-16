package me.andrejov.neswarden;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JoinHandler extends Executor implements Listener {
    Core plugin;

    File listFile = null;
    FileConfiguration list;

    public JoinHandler(Core plugin)
    {
        super(plugin);
        this.plugin = plugin;

        this.loadConfig();

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    public void loadConfig()
    {
        if(listFile == null)
        {
            this.listFile = new File(this.plugin.getDataFolder(), "joins.yml");
        }

        this.list = YamlConfiguration.loadConfiguration(this.listFile);
    }

    public void saveConfig()
    {
        if(listFile == null)
        {
            this.loadConfig();
        }

        try {
            this.list.save(this.listFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this method everytime player joins or changes worlds
     * @param player
     */
    public void onPlayer(Player player)
    {
        boolean action = appendAction(player, "joinw");
        // plugin.getLogger().info("act " + action);

        if(action)
        {
            String cmd = plugin.getConfig().getString("action-joinw-" + player.getWorld().getName());

            // plugin.getLogger().info("CMD: " + cmd);

            if(cmd != null)
            {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        plugin.getUtil().execString(cmd, player);
                    }
                }, 10L);
            }

            // this.plugin.getServer().dispatchCommand(
            //     this.plugin.getServer().getConsoleSender(),
            //     "uc joinw " + player.getName()
            // );
        }
    }

    public boolean appendAction(Player p, String action)
    {
        String keyname = action + "-" + p.getWorld().getName();

        List<String> players = (List<String>) this.list.getList(keyname);

        if(players == null)
        {
            players = new ArrayList<>();
            players.add(p.getName());

            this.list.set(keyname, players);
            this.saveConfig();

            return true;
        }else{
            if(!players.contains(p.getName()))
            {
                players.add(p.getName());

                this.list.set(keyname, players);
                this.saveConfig();

                return true;
            }
        }

        return false;
    }

    public int clearActions(String action)
    {
        return this.clearActions(action, null);
    }

    public int clearActions(String action, String world)
    {
        if(world != null)
        {
            String keyname = action + "-" + world;

            List<?> existing = list.getList(keyname);

            if(existing != null)
            {
                int size = existing.size();

                list.set(keyname, new ArrayList<>());
                this.saveConfig();

                return size;
            }

            return 0;
        }else{
            String keybase = action + "-";

            Set<String> keyset = list.getKeys(false);
            String[] keys = keyset.toArray(new String[keyset.size()]);

            int count = 0;

            for (String key : keys) {
                if(key.startsWith(keybase))
                {
                    count += list.getList(key).size();

                    list.set(key, new ArrayList<>());
                }
            }

            this.saveConfig();

            return count;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        this.onPlayer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e)
    {
        String from = e.getFrom().getWorld().getName();
        String to = e.getTo().getWorld().getName();

        if(!from.equals(to))
        {
            this.onPlayer(e.getPlayer());
        }
    }

    @Override
    public boolean execute(CommandSender sender, Command command, String lcname, String label, String[] args) {

        if(lcname.equals("joins"))
        {
            if(plugin.getUtil().permCheck(sender, "neswarden.joins"))
            {
                return true;
            }
            
            int removed = -1;

            if(args.length == 0)
            {
                plugin.getUtil().usage(sender, lcname, new String[] {"<action> [world]"}, 0);

                return true;
            }else if(args.length == 1){
                removed = this.clearActions(args[0]);
            }else if(args.length == 2){
                removed = this.clearActions(args[0], args[1]);
            }

            sender.sendMessage(ChatColor.GREEN + "Removed " + removed + " entries.");

            return true;
        }

        return false;
    }
}
