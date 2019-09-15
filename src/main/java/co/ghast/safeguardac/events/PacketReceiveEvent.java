package co.ghast.safeguardac.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@Getter
public class PacketReceiveEvent implements Cancellable {
    private Player player;
    private Object packet;
    private boolean cancelled;
    private String type;

    public PacketReceiveEvent(Player player, Object packet, String type) {
        this.player = player;
        this.packet = packet;
        this.type = type;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
