package co.ghast.safeguardac.commands.gui;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.util.GUI;
import co.ghast.safeguardac.util.StringUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ReportGUI implements Listener{
    @Getter private GUI gui = new GUI(null, "Report Settings", 27);



    private SafeGuard sg;
    private byte stainedNumber = 0;

    public ReportGUI (SafeGuard sg) {
        this.sg = sg;
        this.sg.registerListener(this);
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(gui.getInventory().getName())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                Player player = (Player)event.getWhoClicked();
                if (player.hasPermission(StringUtil.ADMIN_PERM)) {
                    if (!event.getCurrentItem().hasItemMeta())
                        return;



                }
            }
        }
    }
}
