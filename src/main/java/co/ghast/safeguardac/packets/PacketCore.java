package co.ghast.safeguardac.packets;

import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.packets.handlers.UseEntityPacketHandler;
import com.comphenix.protocol.ProtocolLibrary;

public class PacketCore {

    public SafeGuard sg;

    public PacketCore(SafeGuard sg) {
        this.sg = sg;
        ProtocolLibrary.getProtocolManager().addPacketListener(new UseEntityPacketHandler(sg));
    }
}
