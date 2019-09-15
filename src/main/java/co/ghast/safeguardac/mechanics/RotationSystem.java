package co.ghast.safeguardac.mechanics;

import co.ghast.safeguardac.SafeGuard;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RotationSystem {
    private SafeGuard sg;
    private Random rand;
    public RotationSystem(SafeGuard sg){
        this.sg = sg;

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(sg, new BukkitRunnable() {

            @Override
            public void run() {
                rotatePlayers();
            }

        }, 20, 20);
    }

    @Getter public HashMap<Player, Long> rotation = new HashMap<>();
    @Getter public ArrayList<Player> rotatingPlayers = new ArrayList<>();
    @Getter @Setter public boolean rotating;
    public void rotatePlayers()  {
        if (this.rotating){
            Player player1 = rotatingPlayers.get(1);
            if (rotation.get(player1) >= (System.currentTimeMillis() + 20 * 1000)){
                this.rotating = false;
                rotatingPlayers.clear();
                rotation.clear();
            }


        } else {
            for (int i=0; i <= sg.getSettingsConfig().getRotatingPlayers(); i++){
                ArrayList<Player> players = new ArrayList<Player>(sg.getServer().getOnlinePlayers());
                if (i >= 1) { // Check if user is already in the list to prevent dupes
                    for (int s = i; s <= rotatingPlayers.size(); s++){
                        players.remove(rotatingPlayers.get(s));
                    }
                }
                int listSize = players.size() - 1;
                int random = new Random().nextInt(listSize);
                Player randomPlayer = players.get(random);
                this.rotation.put(randomPlayer, System.currentTimeMillis());
                this.rotatingPlayers.add(randomPlayer);

            }
            this.rotating = true;
        }
    }


}
