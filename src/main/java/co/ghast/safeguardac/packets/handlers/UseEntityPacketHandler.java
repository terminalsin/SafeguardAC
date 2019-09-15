package co.ghast.safeguardac.packets.handlers;

import co.ghast.safeguardac.events.PacketUseEntityEvent;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class UseEntityPacketHandler extends PacketAdapter {

    public UseEntityPacketHandler(Plugin plugin) {
        super(plugin, PacketType.Play.Client.USE_ENTITY);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        int entityId = event.getPacket().getIntegers().read(0);
        EnumWrappers.EntityUseAction action = null;

        try {
            action = event.getPacket().getEntityUseActions().read(0);
        } catch (Exception e) {
            // 1.7.10 ProtocolLib Stupidity
        }

        Entity selectedEntity = null;

        ImmutableList<Entity> lockedEntityList
                = ImmutableList.copyOf(player.getWorld().getEntities());

        for (Entity en : lockedEntityList) {
            if (en.getEntityId() == entityId) {
                selectedEntity = en;
            }
        }

        if (selectedEntity != null && action != null) {
            Bukkit.getPluginManager().callEvent(new PacketUseEntityEvent(action, player, selectedEntity));
        }

    }
}
