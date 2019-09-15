package co.ghast.safeguardac.mechanics;

import co.ghast.safeguardac.SafeGuard;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DebugSystem {

    private SafeGuard sg;
    @Getter private static String debugPrefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Debug" + ChatColor.GRAY + "] ";
    @Getter  private static HashMap<Player, String> debugPlayers = new HashMap<>();
    public DebugSystem(SafeGuard sg) {
        this.sg = sg;

    }

    public static void addDebugPlayer(Player player, String type){
        debugPlayers.put(player, type);
        player.sendMessage(debugPrefix + ChatColor.YELLOW + "Succesfully enabled debug for: " + type);

    }
    public static void removeDebugPlayer(Player player){
        debugPlayers.remove(player);
        player.sendMessage(debugPrefix + ChatColor.RED + "Succesfully disabled debug");

    }
    public boolean isPlayerDebug(Player player){

        if (this.debugPlayers.containsKey(player)){
            return true;
        } else {
            return false;
        }
    }

}
