package co.ghast.safeguardac.handlers;

import co.ghast.safeguardac.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlertHandler implements Listener {

    private ArrayList<UUID> alertsOn = new ArrayList<>();

    public AlertHandler() {
        for (Player op : Bukkit.getOnlinePlayers()) {
            if (op.hasPermission(StringUtil.STAFF_PERM)|| op.getPlayer().isOp()) {
                alertsOn.add(op.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission(StringUtil.STAFF_PERM) || e.getPlayer().isOp()) {
            this.alertsOn.add(e.getPlayer().getUniqueId());
            e.getPlayer().sendMessage("Added to alert list");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e ) {
        if (hasAlertsOn(e.getPlayer()) || e.getPlayer().isOp()) {
            this.alertsOn.remove(e.getPlayer().getUniqueId());
        }
    }

    public boolean hasAlertsOn(Player player) {
        return this.alertsOn.contains(player.getUniqueId());
    }

    public void toggleAlerts(Player player) {
        if (this.hasAlertsOn(player)) {
            this.alertsOn.remove(player.getUniqueId());
        } else {
            this.alertsOn.add(player.getUniqueId());
        }
    }

    public List<Player> getAlertable() {
        List<Player> alertable = new ArrayList<>();
        for (UUID uuid : alertsOn) {
            alertable.add(Bukkit.getPlayer(uuid));
        }
        return alertable;
    }
}
