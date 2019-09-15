package co.ghast.safeguardac.util;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class BoundingBox {
    public float minX, minY, minZ, maxX, maxY, maxZ;

    public BoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox(Vector min, Vector max) {
        this.minX = (float) Math.min(min.getX(), max.getX());
        this.minY = (float) Math.min(min.getY(), max.getY());
        this.minZ = (float) Math.min(min.getZ(), max.getZ());
        this.maxX = (float) Math.max(min.getX(), max.getX());
        this.maxY = (float) Math.max(min.getY(), max.getY());
        this.maxZ = (float) Math.max(min.getZ(), max.getZ());
    }

    public BoundingBox add(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox add(Vector vector) {
        float x = (float) vector.getX(), y = (float) vector.getY(), z = (float) vector.getZ();

        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox grow(float x, float y, float z) {
        float newMinX = minX - x;
        float newMaxX = maxX + x;
        float newMinY = minY - y;
        float newMaxY = maxY + y;
        float newMinZ = minZ - z;
        float newMaxZ = maxZ + z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox shrink(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX - x;
        float newMinY = minY + y;
        float newMaxY = maxY - y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ - z;

        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox add(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }

    public BoundingBox subtract(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new BoundingBox(this.minX - minX, this.minY - minY, this.minZ - minZ, this.maxX - maxX, this.maxY - maxY, this.maxZ - maxZ);
    }

    public boolean intersectsWithBox(Vector vector) {
        return (vector.getX() > this.minX && vector.getX() < this.maxX) && ((vector.getY() > this.minY && vector.getY() < this.maxY) && (vector.getZ() > this.minZ && vector.getZ() < this.maxZ));
    }

    /*public List<BoundingBox> getCollidingBlockBoxes(Player player) {
        return SafeGuard.getInstance().getBlockBoxManager().getBlockBox().getCollidingBoxes(player.getWorld(), this);
    }*/

    public Vector getMinimum() {
        return new Vector(minX, minY, minZ);
    }

    public Vector getMaximum() {
        return new Vector(maxX, maxY, maxZ);
    }

    /*public List<Block> getCollidingBlocks(Player player) {
        List<Block> toReturn = new ArrayList<>();

        Fiona.getInstance().getBlockBoxManager().getBlockBox().getCollidingBoxes(player.getWorld(), this).forEach(bb -> bb.getMinimum().toLocation(player.getWorld()).getBlock());
        return toReturn;
    }*/


    public List<Block> getAllBlocks(Player player) {
        Location min = new Location(player.getWorld(), MathUtil.floor(minX), MathUtil.floor(minY), MathUtil.floor(minZ));
        Location max = new Location(player.getWorld(), MathUtil.floor(maxX), MathUtil.floor(maxY), MathUtil.floor(maxZ));
        List<Block> all = Lists.newArrayList();
        for (float x = (float) min.getX(); x < max.getX(); x++) {
            for (float y = (float) min.getY(); y < max.getY(); y++) {
                for (float z = (float) min.getZ(); z < max.getZ(); z++) {
                    Block block = BlockUtil.getBlock(new Location(player.getWorld(), x, y, z));
                    if (!block.getType().equals(Material.AIR)) {
                        all.add(block);
                    }
                }
            }
        }
        return all;
    }

    /*public boolean inBlock(Player player) {
        return getCollidingBlocks(player).size() > 0;
    }*/

    public boolean intersectsWithBox(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        } else {
            float otherMinX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX > minX && otherMinX < maxX && otherMaxY > minY && otherMinY < maxY && otherMaxZ > minZ && otherMinZ < maxZ;
        }
    }

    public boolean collides(Vector vector) {
        return (vector.getX() >= this.minX && vector.getX() <= this.maxX) && ((vector.getY() >= this.minY && vector.getY() <= this.maxY) && (vector.getZ() >= this.minZ && vector.getZ() <= this.maxZ));
    }

    public boolean collides(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX >= this.minX && otherBox.minX <= this.maxX && otherBox.maxY >= this.minY && otherBox.minY <= this.maxY && otherBox.maxZ >= this.minZ && otherBox.minZ <= this.maxZ;
        } else {
            float otherMinX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX >= minX && otherMinX <= maxX && otherMaxY >= minY && otherMinY <= maxY && otherMaxZ >= minZ && otherMinZ <= maxZ;
        }
    }

    public boolean collidesHorizontally(Vector vector) {
        return (vector.getX() >= this.minX && vector.getX() <= this.maxX) && ((vector.getY() > this.minY && vector.getY() < this.maxY) && (vector.getZ() >= this.minZ && vector.getZ() <= this.maxZ));
    }

    public boolean collidesHorizontally(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX >= this.minX && otherBox.minX <= this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ >= this.minZ && otherBox.minZ <= this.maxZ;
        } else {
            float otherMinX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX >= minX && otherMinX <= maxX && otherMaxY > minY && otherMinY < maxY && otherMaxZ >= minZ && otherMinZ <= maxZ;
        }
    }

    public boolean collidesVertically(Vector vector) {
        return (vector.getX() > this.minX && vector.getX() < this.maxX) && ((vector.getY() >= this.minY && vector.getY() <= this.maxY) && (vector.getZ() > this.minZ && vector.getZ() < this.maxZ));
    }

    public boolean collidesVertically(Object other) {
        if (other instanceof BoundingBox) {
            BoundingBox otherBox = (BoundingBox) other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY >= this.minY && otherBox.minY <= this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        } else {
            float otherMinX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX > minX && otherMinX < maxX && otherMaxY >= minY && otherMinY <= maxY && otherMaxZ > minZ && otherMinZ < maxZ;
        }
    }

    public Object toAxisAlignedBB() {
        return ReflectionsUtil.newAxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
    public BoundingBox toBounding(AxisAlignedBB aaBB) {return ReflectionsUtil.toBoundingBox(aaBB);}

    public String toString() {
        return "[" + minX + ", " + minY + ", " + minZ + ", " + maxX + ", " + maxY + ", " + maxZ + "]";
    }
    public BoundingBox getBoundingBox(Entity entity){


        /*Vector min = new Vector(bb.a,bb.b,bb.c);
        Vector max = new Vector(bb.d,bb.e,bb.f);
        float x = (float) min.getX(), y = (float) min.getY(), z = (float) min.getZ();
        BoundingBox entityBox = new BoundingBox((Vector) min,(Vector) max);*/
        AxisAlignedBB box = (AxisAlignedBB) ReflectionsUtil.getBoundingBox(entity);
        Vector min = new Vector(box.a, box.b, box.c);
        Vector max = new Vector(box.d, box.e, box.f);
        float minX = min.getBlockX();
        float minY = min.getBlockY();
        float minZ = min.getBlockZ();
        float maxX = max.getBlockX();
        float maxY = max.getBlockY();
        float maxZ = max.getBlockZ();
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);

    }
}
