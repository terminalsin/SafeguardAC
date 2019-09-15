package co.ghast.safeguardac.data;

import cc.funkemunky.api.utils.BoundingBox;
import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.util.PastLocation;
import com.google.common.collect.Lists;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DataPlayer {

    public Player player;
    public boolean onGround, inLiquid, onStairSlab, onIce, onClimbable, underBlock, onSlime;
    public int airTicks, groundTicks, iceTicks, liquidTicks, blockTicks, slimeTicks, velXTicks, velYTicks, velZTicks;
    public long lastVelocityTaken, lastAttack, lastServerKP, ping;
    public Entity lastHitEntity;
    public PastLocation entityPastLocations = new PastLocation();

    /** Player Bounding **/
    public BoundingBox boundingBox, fromBoundingBox;

    /** Killaura **/
    public int killauraAVerbose;
    public long lastFlying;

    /** Pattern **/
    public List<Float> patterns = Lists.newArrayList();
    public float lastRange;
    /**
     * Thresholds
     **/
    public int speedThreshold;
    public int reachThreshold;

    public DataPlayer(Player player) {
        this.player = player;
        boundingBox = fromBoundingBox = new BoundingBox(0, 0, 0, 0, 0, 0);
        new BukkitRunnable() {
            public void run() {
                if(lastHitEntity != null) {
                    entityPastLocations.addLocation(lastHitEntity.getLocation());
                }
            }
        }.runTaskTimer(SafeGuard.getInstance(), 0L, 1L);
    }

    public boolean isVelocityTaken() {
        return velXTicks > 0 || velYTicks > 0 || velZTicks > 0;
    }

    public void reduceVelocity() {
        velXTicks = Math.max(0, velXTicks - 1);
        velYTicks = Math.max(0,velYTicks - 1);
        velZTicks = Math.max(0, velZTicks - 1);
    }

}