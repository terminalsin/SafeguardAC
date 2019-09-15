package co.ghast.safeguardac.events;

import co.ghast.safeguardac.SafeGuard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class PlayerOpenInventoryEvent implements Listener{


    private SafeGuard sg;
    public PlayerOpenInventoryEvent(SafeGuard sg){
        this.sg = sg;
    }



    @EventHandler
    public void openInventory(InventoryOpenEvent e){

    }



}
