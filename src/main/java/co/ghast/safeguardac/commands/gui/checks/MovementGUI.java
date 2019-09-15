package co.ghast.safeguardac.commands.gui.checks;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.checks.Check;
import co.ghast.safeguardac.checks.CheckCategory;
import co.ghast.safeguardac.checks.SetChecks;
import co.ghast.safeguardac.util.GUI;
import co.ghast.safeguardac.util.ItemBuilder;
import co.ghast.safeguardac.util.StringUtil;
import co.ghast.safeguardac.util.Useful;

public class MovementGUI implements Listener {

    @Getter private GUI checkToggleGUI = new GUI(null, "Toggling M Checks", 54);
    @Getter private GUI banToggleGUI = new GUI(null, "Toggling M Bans", 54);

    private SafeGuard sg;

    public MovementGUI(SafeGuard sg) {
        this.sg = sg;
        this.sg.registerListener(this);

        loadItems();
    }

    public void loadItems() {
        int i = 0;
        for (Check check : sg.getChecks()) {
            if (check.getCheckCategory() == CheckCategory.MOVEMENT) {
                this.checkToggleGUI.setItem(i, check.isEnabled() ? ItemBuilder.buildItem(Material.EMERALD_BLOCK, "&a"+check.getName()) :
                        ItemBuilder.buildItem(Material.REDSTONE_BLOCK, "&c" + check.getName()));
                this.banToggleGUI.setItem(i, check.isBannable() ? ItemBuilder.buildItem(Material.EMERALD_BLOCK, "&a"+check.getName()) :
                        ItemBuilder.buildItem(Material.REDSTONE_BLOCK, "&c" + check.getName()));
                i++;
            }
        }
        this.checkToggleGUI.setItem(53, ItemBuilder.buildItem(Material.WOOD_DOOR, "&b&lBACK"));
        this.banToggleGUI.setItem(53, ItemBuilder.buildItem(Material.WOOD_DOOR, "&b&lBACK"));
    }

    @EventHandler
    public void onGUIClick(InventoryClickEvent event) {
        String invName = event.getInventory().getName();
        if (invName.equals(checkToggleGUI.getInventory().getName()) || invName.equals(banToggleGUI.getInventory().getName())) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                Player player = (Player)event.getWhoClicked();
                if (player.hasPermission(StringUtil.ADMIN_PERM)) {
                    if (!event.getCurrentItem().hasItemMeta())
                        return;

                    String display = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    Check check = Useful.getByName(display);

                    if (check != null) {
                        if (invName.contains("Checks")) {
                            check.setEnabled(!check.isEnabled());
                        }
                        else if (invName.contains("Bans")) {
                            check.setBannable(!check.isBannable());
                        }
                        loadItems();
                        SetChecks.saveCheck(check);
                    }
                }
            }
        }
    }
}
