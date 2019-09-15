package co.ghast.safeguardac.commands.gui.checks;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.commands.gui.MainGUI;
import co.ghast.safeguardac.util.GUI;
import co.ghast.safeguardac.util.ItemBuilder;
import co.ghast.safeguardac.util.StringUtil;

public class ChecksGUI implements Listener {

    @Getter private GUI gui = new GUI(null, "Checks", 27);
    @Getter CombatGUI combatGUI;
    @Getter MovementGUI movementGUI;
    @Getter OtherGUI otherGUI;

    private SafeGuard sg;

    public ChecksGUI(SafeGuard sg) {
        this.sg = sg;
        this.sg.registerListener(this);

        this.combatGUI = new CombatGUI(sg);
        this.movementGUI = new MovementGUI(sg);
        this.otherGUI = new OtherGUI(sg);

        loadItems();
    }

    public void loadItems() {
        this.gui.setItem(11, ItemBuilder.buildItem(Material.WOOD_SWORD, "&eCombat Checks"));
        this.gui.setItem(13, ItemBuilder.buildItem(Material.LEATHER_BOOTS, "&eMovement Checks"));
        this.gui.setItem(15, ItemBuilder.buildItem(Material.PAPER, "&eOther Checks"));
        this.gui.setItem(26, ItemBuilder.buildItem(Material.WOOD_DOOR, "&b&lBACK"));
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

                    MainGUI.ToggleType type = sg.getMainGUI().getEditType().get(player.getName());
                    String display = event.getCurrentItem().getItemMeta().getDisplayName();

                    if (display.contains("Combat Checks")) {
                        player.openInventory(type == MainGUI.ToggleType.CHECK_TOGGLE ? combatGUI.getCheckToggleGUI().getInventory() : combatGUI.getBanToggleGUI().getInventory());
                    }
                    else if (display.contains("Movement Checks")) {
                        player.openInventory(type == MainGUI.ToggleType.CHECK_TOGGLE ? movementGUI.getCheckToggleGUI().getInventory() : movementGUI.getBanToggleGUI().getInventory());
                    }
                    else if (display.contains("Other Checks")) {
                        player.openInventory(type == MainGUI.ToggleType.CHECK_TOGGLE ? otherGUI.getCheckToggleGUI().getInventory() : otherGUI.getBanToggleGUI().getInventory());
                    }
                    else if (display.contains("BACK")) {
                        player.openInventory(sg.getMainGUI().getGui().getInventory());
                    }
                }
            }
        }
        else if (event.getInventory().getName().contains("Toggling")) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() == Material.WOOD_DOOR) {
                    event.getWhoClicked().openInventory(gui.getInventory());
                }
            }
        }
    }
}
