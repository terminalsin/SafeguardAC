package co.ghast.safeguardac.checks.combat.killaura;

import cc.funkemunky.api.events.Listen;
import cc.funkemunky.api.events.impl.PacketReceiveEvent;
import cc.funkemunky.api.tinyprotocol.api.Packet;
import cc.funkemunky.api.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import co.ghast.safeguardac.SafeGuard;
import co.ghast.safeguardac.checks.Check;
import co.ghast.safeguardac.checks.CheckCategory;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class KillAuraB extends Check{
    private SafeGuard sg;
    private HashMap<Player, Location> previousLoc = new HashMap<>();
    private HashMap<Player, Integer> average = new HashMap<>();
    private HashMap<Player, Integer> violated = new HashMap<>();

    public KillAuraB(SafeGuard sg) {
        super("KillAuraB", "KillAura (Type B) [Experimental]", CheckCategory.COMBAT, sg);
        this.setEnabled(true);
        this.sg = sg;
    }

    @Listen
    public void onPacket(PacketReceiveEvent e){
        if(e.getType()  .equals(Packet.Client.LOOK) && e.getType().equals(Packet.Client.USE_ENTITY))  {
            WrappedInUseEntityPacket packet = new WrappedInUseEntityPacket(e.getPacket(), e.getPlayer());
            Player attacker = packet.getPlayer();
            if (!(packet.getEntity() instanceof LivingEntity)) return;
            LivingEntity attacked = (LivingEntity) packet.getEntity();
            if (packet.getAction() != WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) return;
            if (attacker.isFlying()) return;
            if (attacker.getGameMode() == GameMode.CREATIVE) return;
            float yaw = attacker.getLocation().getYaw();
            float pitch = attacker.getLocation().getPitch();
            Location loc = attacker.getLocation();
            if (previousLoc.containsKey(attacker)){
                float prevPitch = previousLoc.get(attacker).getPitch();
                float prevYaw = previousLoc.get(attacker).getYaw();

                if (prevPitch == pitch && prevYaw == yaw){

                    if (this.violated.containsKey(attacker)) {
                        int av = this.violated.get(attacker);
                        this.violated.remove(attacker);
                        this.violated.put(attacker, av + 1);
                    } else {
                        this.violated.put(attacker, 1);
                    }

                    if (this.average.containsKey(attacker)) {
                        int av = this.average.get(attacker);
                        this.average.remove(attacker);
                        this.average.put(attacker, av + 1);
                    }
                    else {
                        this.average.put(attacker, 1);
                    }

                } else {
                    if (this.average.containsKey(attacker)) {
                        int av = this.average.get(attacker);
                        this.average.remove(attacker);
                        this.average.put(attacker, av - 1);
                    }
                }


            }
            if (violated.get(attacker) > 10) {
                double average = this.average.get(attacker);
                this.average.remove(attacker);
                this.violated.remove(attacker);
                if (average > 7){
                    sg.getVlhandler().logCheat(this, attacker, ("Average: " + Double.toString(average)));
                    return;
                }

            }



        }


    }
}
