package co.ghast.safeguardac.update.events;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import co.ghast.safeguardac.update.UpdateType;

public class UpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private UpdateType Type;

    public UpdateEvent(UpdateType Type) {
        this.Type = Type;
    }

    public UpdateType getType() {
        return this.Type;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}