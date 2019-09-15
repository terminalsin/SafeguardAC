package co.ghast.safeguardac.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class VelocityEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Vector vec;

    public VelocityEvent(Player player, Vector vec) {
        this.player = player;
        this.vec = vec;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Vector getVec() {
        return vec;
    }
}
