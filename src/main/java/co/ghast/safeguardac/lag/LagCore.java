package co.ghast.safeguardac.lag;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.util.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class LagCore implements Listener {

    public SafeGuard sg;
    private double tps;

    public LagCore(SafeGuard sg) {
        this.sg = sg;

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.sg,
                new Runnable() {
                    long sec;
                    long currentSec;
                    int ticks;

                    @Override
                    public void run() {
                        sec = (System.currentTimeMillis() / 1000);

                        if (currentSec == sec) {
                            ticks++;
                        } else {
                            currentSec = sec;
                            tps = (tps == 0 ? ticks : ((tps + ticks) / 2));
                            ticks = 0;
                        }
                    }
                }, 0, 1);

        this.sg.registerListener(this);
    }

    public double getTPS() {
        return ((this.tps + 1) > 20) ? 20 : this.tps + 1;
    }

    public static int getPing(Player player) {
        Object handle = ReflectionUtil.getPlayerHandle(player);

        if (handle != null) {
            Field ping = ReflectionUtil.getField("ping", handle.getClass());
            try {
                return ping.getInt(handle);
                //return (int) handle.getClass().getField("ping").get(handle);
            } catch (Exception e) {
                //e.printStackTrace();
                // Why Print Errors on Ping?
                return -1;
            }
        } else {
            return -1;
        }
    }
}
