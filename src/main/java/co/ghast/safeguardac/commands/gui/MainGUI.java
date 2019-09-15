package co.ghast.safeguardac.commands.gui;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.commands.gui.checks.ChecksGUI;
import co.ghast.safeguardac.util.GUI;
import co.ghast.safeguardac.util.ItemBuilder;
import co.ghast.safeguardac.util.StringUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.WeakHashMap;

public class MainGUI implements Listener {

    @Getter private GUI gui = new GUI(null, "JI Settings", 27);
    @Getter private Map<String, ToggleType> editType;
    @Getter private ChecksGUI checksGUI;


    private SafeGuard sg;
    private byte stainedNumber = 0;

    public MainGUI(SafeGuard sg) {
        this.sg = sg;
        this.sg.registerListener(this);

        this.editType = new WeakHashMap<>();
        this.checksGUI = new ChecksGUI(sg);

        loadItems();
        changeItems();
    }


    public void changeItems(){
        new BukkitRunnable(){
            @Override
            public void run() {
                // What you want to schedule goes here

                if (stainedNumber == 16){
                    stainedNumber = 0;
                }

                gui.setItem(0, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " ", stainedNumber));
                gui.setItem(8, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " ", stainedNumber));
                gui.setItem(9, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " ", stainedNumber));
                gui.setItem(17, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " ", stainedNumber));
                gui.setItem(18, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " ", stainedNumber));
                gui.setItem(26, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " ", stainedNumber));
                stainedNumber++;


            }
        }.runTaskTimer(this.sg, 20, 20);

    }

    public void loadItems() {
        for (int i = 0; i < 11; i++){
            this.gui.setItem(i, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " "));
        }
        for (int i = 16; i < 27; i++){
            this.gui.setItem(i, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " "));
        }
        this.gui.setItem(12, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " "));
        this.gui.setItem(14, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, " "));

        this.gui.setItem(15, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, "&4COMING SOON!", (byte)14));
        this.gui.setItem(11, ItemBuilder.buildItem(Material.BOOK_AND_QUILL, "&eToggle Checks", "", "&7Enable/Disable checks", ""));
        this.gui.setItem(13, ItemBuilder.buildItem(Material.BOOK_AND_QUILL, "&eToggle Punishments", "", "&7Enable/Disable check punishments"));
        //this.gui.setItem(15, sg.isJday() ? ItemBuilder.buildItem(Material.EMERALD_BLOCK, "&aJday enabled", "", "&7Click to disable", "") :
        //ItemBuilder.buildItem(Material.DISPENSER.REDSTONE_BLOCK, "&cJday disabled", "", "&7Click to enable", "");
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

                    String display = event.getCurrentItem().getItemMeta().getDisplayName();
                    if (display.contains("Toggle Checks")) {
                        editType.put(player.getName(), ToggleType.CHECK_TOGGLE);
                        this.open(player);
                    }
                    else if (display.contains("Toggle Punishments")) {
                        editType.put(player.getName(), ToggleType.PUNISHMENT_TOGGLE);
                        this.open(player);
                    }
                    /*else if (display.contains("Jday")) {
                        boolean jday = sg.isJday() ? false : true;
                        sg.getConfig().set("JDayConfig", jday);
                        sg.saveConfig();
                        sg.setJday(jday);
                        loadItems();
                    }*/
                }
            }
        }
    }

    public void open(Player player) {
        player.openInventory(checksGUI.getGui().getInventory());
    }

    public enum ToggleType {
        CHECK_TOGGLE, PUNISHMENT_TOGGLE;
    }
}
