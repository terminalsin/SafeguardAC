package co.ghast.safeguardac.util;


import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {

    public static void clear(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);

        try {
            Object handle = ReflectionUtil.getPlayerHandle(player);

            if (handle != null) {
                Object inventory = handle.getClass().getField("inventory").get(handle);
                Object tagList = ReflectionUtil.getNMSClass("NBTTagList").newInstance();
                inventory.getClass().getMethod("b", tagList.getClass()).invoke(inventory, tagList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.setSprinting(false);

        player.setFoodLevel(20);
        player.setSaturation(3.0F);
        player.setExhaustion(0.0F);

        player.setMaxHealth(20.0D);
        player.setHealth(player.getMaxHealth());

        player.setFireTicks(0);
        player.setFallDistance(0.0F);

        player.setLevel(0);
        player.setExp(0.0F);

        player.setWalkSpeed(0.2F);
        player.setFlySpeed(0.1F);

        player.getInventory().clear();

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        player.updateInventory();

        for (PotionEffect potion : player.getActivePotionEffects()) {
            player.removePotionEffect(potion.getType());
        }
    }

    public static double distanceXZ(Location from, Location to) {
        double deltaX = from.getX() - to.getX();
        double deltaZ = from.getZ() - to.getZ();

        return Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));
    }

    public static Location getEyeLocation(Player player) {
        Location eye = player.getLocation();

        eye.setY(eye.getY() + player.getEyeHeight());

        return eye;
    }

    public static boolean isInWater(Player player) {
        Material m = player.getLocation().getBlock().getType();
        if (m == Material.STATIONARY_WATER || m == Material.WATER)
            return true;

        return false;
    }

    public static boolean isOnClimbable(Player player) {
        for (Block block : BlockUtil.getSurrounding(player.getLocation().getBlock(), false)) {
            if (block.getType() == Material.LADDER || block.getType() == Material.VINE) {
                return true;
            }
        }

        if (player.getLocation().getBlock().getType() == Material.LADDER ||
                player.getLocation().getBlock().getType() == Material.VINE)
            return true;

        return false;
    }

    public static boolean isOnGround(Player player) {
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
            return true;

        Location a = player.getLocation().clone();
        a.setY(a.getY() - 0.5);
        if (a.getBlock().getType() != Material.AIR)
            return true;

        a = player.getLocation().clone();
        a.setY(a.getY() + 0.5);
        if (a.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
            return true;

        if (CheatUtil.isBlock(player.getLocation().getBlock().getRelative(BlockFace.DOWN),
                new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER,}))
            return true;

        return false;
    }

    // Player
    private static Method GET_HANDLE = null;

    // Bounding Box
    private static Field BOUNDINGBOX_FIELD = null;

    /*public static boolean boundingBoxOnGround(Player player) {
        //Object aabb = getPlayerAABB(player);
        if (!CompatHandler.is17()) {
            AxisAlignedBB axisalignedbb = ((Player)player).getHandle().getBoundingBox()
                    .grow(0.0625F, 0.0625F, 0.0625F)
                    .a(0.0D, -0.2D, 0.0D);

            World cw = (World) player.getWorld();
            return cw.getHandle().c(axisalignedbb);
        }

        return true;
    }*/
    public static boolean boundingBoxOnGround(Player player) {
        //Object aabb = getPlayerAABB(player);
        if (!CompatHandler.is17()) {
            AxisAlignedBB axisalignedbb = ((CraftPlayer)player).getHandle().getBoundingBox()
                    .grow(0.0625F, 0.0625F, 0.0625F)
                    .a(0.0D, -0.2D, 0.0D);

            CraftWorld cw = (CraftWorld) player.getWorld();
            return cw.getHandle().c(axisalignedbb);
        }

        return true;
    }

    public static Object getPlayerAABB(Player player) {
        try {
            if (GET_HANDLE == null) {
                GET_HANDLE = ReflectionUtil.getMethod("getHandle", player.getClass());
            }

            Object nmsPlayer = GET_HANDLE.invoke(player);

            if (BOUNDINGBOX_FIELD == null) {
                BOUNDINGBOX_FIELD = ReflectionUtil.getField(
                        "boundingBox", nmsPlayer.getClass());
                BOUNDINGBOX_FIELD.setAccessible(true);
            }

            Object boundingBox = BOUNDINGBOX_FIELD.get(nmsPlayer);

            /*if (BOUNDINGBOX_A == null) {
                BOUNDINGBOX_A = ReflectionUtil.getField("a", boundingBox.getClass());
                BOUNDINGBOX_A.setAccessible(true);
            }

            if (BOUNDINGBOX_B == null) {
                BOUNDINGBOX_B = ReflectionUtil.getField("b", boundingBox.getClass());
                BOUNDINGBOX_B.setAccessible(true);
            }

            if (BOUNDINGBOX_C == null) {
                BOUNDINGBOX_C = ReflectionUtil.getField("c", boundingBox.getClass());
                BOUNDINGBOX_C.setAccessible(true);
            }

            if (BOUNDINGBOX_D == null) {
                BOUNDINGBOX_D = ReflectionUtil.getField("d", boundingBox.getClass());
                BOUNDINGBOX_D.setAccessible(true);
            }

            if (BOUNDINGBOX_E == null) {
                BOUNDINGBOX_E = ReflectionUtil.getField("e", boundingBox.getClass());
                BOUNDINGBOX_E.setAccessible(true);
            }

            if (BOUNDINGBOX_F == null) {
                BOUNDINGBOX_F = ReflectionUtil.getField("f", boundingBox.getClass());
                BOUNDINGBOX_F.setAccessible(true);
            }

            double minX = BOUNDINGBOX_A.getDouble(boundingBox);
            double minY = BOUNDINGBOX_B.getDouble(boundingBox);
            double minZ = BOUNDINGBOX_C.getDouble(boundingBox);
            double maxX = BOUNDINGBOX_D.getDouble(boundingBox);
            double maxY = BOUNDINGBOX_E.getDouble(boundingBox);
            double maxZ = BOUNDINGBOX_F.getDouble(boundingBox);

            return new AxisAlignedBB(minX - 0.5, minY - 0.2, minZ, maxX + 0.5,
                    maxY + 0.2, maxZ + 0.2);*/
            return boundingBox;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Entity> getNearbyRidables(Location loc, double distance) {
        List<Entity> entities = new ArrayList<>();

        for (Entity entity : new ArrayList<>(loc.getWorld().getEntities())) {
            if (!(entity.getType().equals(EntityType.HORSE)) && !(entity.getType().equals(EntityType.BOAT)))
                continue;

            Bukkit.getServer().broadcastMessage(entity.getLocation().distance(loc) + "");
            if (entity.getLocation().distance(loc) <= distance) {

                entities.add(entity);
            }
        }

        return entities;
    }
    public static boolean facingOpposite(Entity one, Entity two) {
        return one.getLocation().getDirection().distance(two.getLocation().getDirection()) < 0.5;
    }
}
