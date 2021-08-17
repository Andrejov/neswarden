package me.andrejov.neswarden;

import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Timeout {
    
    Player player;

    int timeout;
    long endtime;

    boolean canMove;
    Location location;

    String action;

    public Timeout(Player p, int time, String action, boolean canMove)
    {
        this.player = p;
        this.timeout = time;
        this.canMove = canMove;
        this.action = action;

        Date now = new Date();

        this.endtime = now.getTime() + time;

        if(!this.canMove)
        {
            this.location = p.getLocation().clone();
        }
    }

    public boolean elapsed()
    {
        Date now = new Date();

        return now.getTime() > this.endtime;
    }

    public boolean hasMoved()
    {
        Location n = this.player.getLocation();
        Location o = this.location;

        return !(
            Math.abs(n.getX() - o.getX()) < 0.5 &&
            Math.abs(n.getY() - o.getY()) < 0.5 &&
            Math.abs(n.getZ() - o.getZ()) < 0.5
        );
    }
}
