package co.ghast.safeguardac.update;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.update.events.UpdateEvent;
import org.bukkit.Bukkit;

public class Updater implements Runnable {
    private SafeGuard sg;
    private int updater;

    public Updater(SafeGuard sg) {
        this.sg = sg;
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.sg, this, 0L, 1L);
    }

    public void Disable() {
        Bukkit.getScheduler().cancelTask(updater);
    }

    public void run() {
        for (UpdateType updateType : UpdateType.values()) {
            if (updateType == null)
                continue;
            if (updateType.Elapsed()) {
                try {
                    UpdateEvent event = new UpdateEvent(updateType);
                    Bukkit.getPluginManager().callEvent(event);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
