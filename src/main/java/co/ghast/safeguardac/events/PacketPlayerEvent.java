package co.ghast.safeguardac.events;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketPlayerEvent extends Event {

    private org.bukkit.entity.Player Player;
    private PacketEvent packetEvent;

    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    private static final HandlerList handlers = new HandlerList();

    private co.ghast.safeguardac.events.PacketPlayerType type;

    public PacketPlayerEvent(org.bukkit.entity.Player Player, PacketEvent packetEvent, double x, double y, double z, float yaw, float pitch, co.ghast.safeguardac.events.PacketPlayerType type) {
        this.Player = Player;
        this.packetEvent = packetEvent;

        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.type = type;
    }

    public org.bukkit.entity.Player getPlayer() {
        return this.Player;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public co.ghast.safeguardac.events.PacketPlayerType getType() {
        return this.type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public PacketEvent getPacketEvent() {
        return packetEvent;
    }
}