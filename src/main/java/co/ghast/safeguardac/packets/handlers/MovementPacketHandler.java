package co.ghast.safeguardac.packets.handlers;

import co.ghast.safeguardac.events.PacketPlayerEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MovementPacketHandler extends PacketAdapter {

    public MovementPacketHandler(Plugin plugin) {
        super(plugin, PacketType.Play.Client.POSITION_LOOK, PacketType.Play.Client.LOOK,
                PacketType.Play.Client.POSITION, PacketType.Play.Client.POSITION,
                PacketType.Play.Client.FLYING);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        PacketType packetType = event.getPacketType();

        if (packetType == PacketType.Play.Client.POSITION_LOOK) {
            Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
                    event, event.getPacket().getDoubles().read(0), event.getPacket().getDoubles().read(1),
                    event.getPacket().getDoubles().read(2), event.getPacket().getFloat().read(0),
                    event.getPacket().getFloat().read(1), co.ghast.safeguardac.events.PacketPlayerType.POSLOOK));
        } else if (packetType == PacketType.Play.Client.LOOK) {
            Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
                    event, player.getLocation().getX(), player.getLocation().getY(),
                    player.getLocation().getZ(), event.getPacket().getFloat().read(0),
                    event.getPacket().getFloat().read(1), co.ghast.safeguardac.events.PacketPlayerType.LOOK));
        } else if (packetType == PacketType.Play.Client.POSITION) {
            Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
                    event, event.getPacket().getDoubles().read(0), event.getPacket().getDoubles().read(1),
                    event.getPacket().getDoubles().read(2), player.getLocation().getYaw(),
                    player.getLocation().getPitch(), co.ghast.safeguardac.events.PacketPlayerType.POSITION));
        } else if (packetType == PacketType.Play.Client.FLYING) {
            Bukkit.getServer().getPluginManager().callEvent(new PacketPlayerEvent(player,
                    event, player.getLocation().getX(), player.getLocation().getY(),
                    player.getLocation().getZ(), player.getLocation().getYaw(),
                    player.getLocation().getPitch(), co.ghast.safeguardac.events.PacketPlayerType.FLYING));
        }
    }
}
