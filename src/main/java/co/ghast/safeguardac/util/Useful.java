package co.ghast.safeguardac.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.checks.Check;

public class Useful {

    public static boolean isOnBlock(Player player) {
        Location a = player.getLocation();
        boolean val = false;

        a.setY(a.getY() - 1);
        if (!(a.getBlock().getType().equals(Material.AIR)))
            val = true;

        a.setY(a.getY() - 1);

        if (!(a.getBlock().getType().equals(Material.AIR)))
            val = true;

        return val;
    }

    public static boolean isOnSuperIce(Player player) {
        Location a = player.getLocation();
        boolean underice = false;
        boolean overblock = false;
        a.setY(a.getY() - 1);
        if (a.getBlock().getType().equals(Material.ICE))
            underice = true;
        a.setY(a.getY() + 3);
        if (!(a.getBlock().getType().equals(Material.AIR)))
            overblock = true;
        return underice && overblock;
    }

    public static double distance(Location loc, Location loc2) {
        return Math.abs(loc.getX() - loc2.getX()) + Math.abs(loc.getZ() - loc2.getZ());
    }

    public static Check getByName(String checkName) {
        for (Check check : SafeGuard.getInstance().getChecks()) {
            if (check.getName().equals(checkName)) {
                return check;
            }
        }
        return null;
    }

}

